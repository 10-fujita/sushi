package com.example.springlesson.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springlesson.entity.CartItem;
import com.example.springlesson.entity.Order;
import com.example.springlesson.entity.OrderItem;
import com.example.springlesson.entity.User;
import com.example.springlesson.form.OrderForm;
import com.example.springlesson.repository.CartItemRepository;
import com.example.springlesson.repository.OrderItemRepository;
import com.example.springlesson.repository.OrderRepository;
import com.example.springlesson.repository.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository,
            CartItemRepository cartItemRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * 注文作成処理（カート → orders / order_items）
     */
    @Transactional
    public void createOrder(
            String email,
            List<CartItem> cartItems,
            OrderForm form) {

      User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new IllegalStateException("ログインユーザーが存在しません。");
        }

        int totalAmount = cartItems.stream()
                .mapToInt(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderDatetime(LocalDateTime.now());
        order.setStatus("ORDERED");
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

        // orders テーブルに保存
        orderRepository.save(order);

        // order_items テーブルに保存
        for (CartItem cartItem : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(cartItem.getProduct());
            item.setProductName(cartItem.getProduct().getName());
            item.setUnitPrice(cartItem.getUnitPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setSubtotalAmount(
                    cartItem.getUnitPrice() * cartItem.getQuantity());

            orderItemRepository.save(item);
            cartItemRepository.delete(cartItem);
        }
    }

    /**
     * 注文履歴取得（メールアドレス）
     */
    @Transactional(readOnly = true)
    public List<Order> findOrdersByEmail(String email) {

        
      userRepository.findByEmail(email);
        return orderRepository.findFirstByUserEmailOrderByOrderDatetimeDesc(email);
                
    }
    //購入処理後カートの内容削除
    @Transactional
    public void clearCart(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
            cartItemRepository.deleteByUserId(user.getId());
        }
    }


