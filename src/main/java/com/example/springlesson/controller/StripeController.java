package com.example.springlesson.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springlesson.entity.CartItem;
import com.example.springlesson.entity.Order;
import com.example.springlesson.entity.OrderItem;
import com.example.springlesson.entity.User;
import com.example.springlesson.form.OrderForm;
import com.example.springlesson.repository.CartItemRepository;
import com.example.springlesson.repository.OrderItemRepository;
import com.example.springlesson.repository.OrderRepository;
import com.example.springlesson.repository.UserRepository;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeController {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final UserRepository userRepository;
  private final CartItemRepository cartItemRepository;
  @Value("${stripe.webhook-secret}")
  private String webhookSecret;

  @Value("${stripe.secret-key}")
  private String stripeSecretKey;

  @PostMapping("/checkout")
  public Map<String, String> checkout(@RequestBody OrderForm form, Principal principal) throws Exception {
    com.stripe.Stripe.apiKey = stripeSecretKey;
    // ユーザー取得
    User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new IllegalStateException("ログインユーザーが存在しません"));

    // カート取得
    List<CartItem> cartItems = cartItemRepository.findByUser(user);

    int totalAmount = cartItems.stream()
        .mapToInt(item -> item.getUnitPrice() * item.getQuantity())
        .sum();

    // 仮注文作成（未払い）
    Order order = new Order();
    order.setUser(user);
    order.setOrderNumber(UUID.randomUUID().toString());
    order.setStatus("CREATED");
    order.setTotalAmount(totalAmount);
    order.setShippingRecipientName(form.getRecipientName());
    order.setShippingPostalCode(form.getPostalCode());
    order.setShippingPrefecture(form.getPrefecture());
    order.setShippingCity(form.getCity());
    order.setShippingAddressLine1(form.getAddressLine1());
    order.setShippingAddressLine2(form.getAddressLine2());
    order.setShippingPhoneNumber(form.getPhoneNumber());
    order.setDeliveryDate(form.getDeliveryDate());
    order.setDeliveryTimeSlot(form.getDeliveryTimeSlot());
    // 【追加】注文日時を現在時刻でセットする
    order.setOrderDatetime(LocalDateTime.now());
    orderRepository.save(order);

    // OrderItem作成
    for (CartItem cartItem : cartItems) {
      OrderItem item = new OrderItem();
      item.setOrder(order);
      item.setProduct(cartItem.getProduct());
      item.setProductName(cartItem.getProduct().getName());
      item.setUnitPrice(cartItem.getUnitPrice());
      item.setSubtotalAmount(cartItem.getUnitPrice() * cartItem.getQuantity());
      item.setQuantity(cartItem.getQuantity());
      orderItemRepository.save(item);
    }

    // Stripe Checkout セッション作成
    SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl("https://sushi-a2gn.onrender.com/springlesson/purchase/purchase-out")
        .setCancelUrl("https://sushi-a2gn.onrender.com/springlesson/cancel")
        .putMetadata("orderId", order.getId().toString()); // ←大文字 M

    for (CartItem cartItem : cartItems) {
      paramsBuilder.addLineItem(
          SessionCreateParams.LineItem.builder()
              .setQuantity((long) cartItem.getQuantity())
              .setPriceData(
                  SessionCreateParams.LineItem.PriceData.builder()
                      .setCurrency("jpy")
                      .setUnitAmount((long) cartItem.getUnitPrice())
                      .setProductData(
                          SessionCreateParams.LineItem.PriceData.ProductData.builder()
                              .setName(cartItem.getProduct().getName())
                              .build())
                      .build())
              .build());
    }

    com.stripe.model.checkout.Session session = com.stripe.model.checkout.Session.create(paramsBuilder.build());

    // フロントに sessionId を返す
    return Map.of("sessionId", session.getId());
  }

  // =========================
  // Stripe Webhookで決済完了
  // =========================
  @PostMapping("/webhook")
  public ResponseEntity<String> handleWebhook(@RequestBody String payload,
      @RequestHeader("Stripe-Signature") String sigHeader) {
    System.out.println("====== Webhook受信チェック開始 ======");
    Event event;
    try {
      event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
    } catch (Exception e) {
      System.err.println("署名検証エラー: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
    }

    if ("checkout.session.completed".equals(event.getType())) {
      System.out.println("--- checkout.session.completed を自力で解析します ---");

      try {
        // Stripe SDKのパースに頼らず、Gsonを使って直接JSONからmetadata.orderIdを引っこ抜く
        com.google.gson.JsonObject json = com.google.gson.JsonParser.parseString(payload).getAsJsonObject();
        com.google.gson.JsonObject sessionObj = json.getAsJsonObject("data").getAsJsonObject("object");
        com.google.gson.JsonObject metadata = sessionObj.getAsJsonObject("metadata");

        if (metadata != null && metadata.has("orderId")) {
          String orderIdStr = metadata.get("orderId").getAsString();
          System.out.println("自力取得成功！ orderId: " + orderIdStr);

          Long orderId = Long.parseLong(orderIdStr);
          orderRepository.findById(orderId).ifPresentOrElse(order -> {
            order.setStatus("ORDERED");
            orderRepository.save(order);
            System.out.println("注文ID " + orderId + " を ORDERED に更新完了！");
          }, () -> System.out.println("エラー: DBに注文ID " + orderId + " がありません。"));
        } else {
          System.out.println("エラー: JSON内のmetadataにorderIdが見つかりません。");
        }
      } catch (Exception e) {
        System.err.println("自力パース中にエラーが発生しました: " + e.getMessage());
        e.printStackTrace();
      }
    }
    return ResponseEntity.ok("");
  }
}
