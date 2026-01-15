package com.example.springlesson.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springlesson.entity.CartItem;
import com.example.springlesson.entity.Order;
import com.example.springlesson.entity.User;
import com.example.springlesson.entity.UserAddress;
import com.example.springlesson.form.OrderForm;
import com.example.springlesson.security.UserDetailsImpl;
import com.example.springlesson.service.CartService;
import com.example.springlesson.service.OrderService;
import com.example.springlesson.service.UserService;

@Controller
public class PurchaseController {

    private final CartService cartService;
    private final UserService userService;
    private final OrderService orderService;

    public PurchaseController(CartService cartService, UserService userService, OrderService orderService) {
        this.cartService = cartService;
        this.userService = userService;
        this.orderService = orderService;
    }

    /**
     * カート画面を表示
     * 既存のメソッド
     */
    @GetMapping("/purchase/cart")
    public String viewCart(@RequestParam String email, Model model) {
        // メールから User を取得
        User user = userService.findByEmail(email);
        // User を元に CartItem を取得
        List<CartItem> cartItems = cartService.findCartItems(user);
        // 合計金額も計算してモデルに追加
        int total = cartService.calcTotal(cartItems);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart/view"; // templates/cart/cart.html を表示
    }

    /**
     * カート → お届け先入力画面
     */
    @GetMapping("/purchase/purchase-inf")
    public String purchaseInf(Model model, @AuthenticationPrincipal UserDetailsImpl principal) {
        String email = principal.getUsername();
        User user = userService.findByEmail(email);
        List<UserAddress> addresses = user.getAddresses();
        model.addAttribute("address1", addresses);

        List<Order> latestOrder = orderService.findOrdersByEmail(email);
        if (latestOrder != null && !latestOrder.isEmpty()) {
            model.addAttribute("address2", latestOrder.get(0));
        } else {
            model.addAttribute("address2", null);
        }

        List<LocalDate> dateList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 31; i++) {
            dateList.add(today.plusDays(i));
        }

        // --- 時間のリスト作成 ---
        List<String> timeSlots = new ArrayList<>();
        for (int h = 10; h <= 22; h++) {
            timeSlots.add(String.format("%02d:00", h));
        }
        java.util.Collections.sort(timeSlots);

        model.addAttribute("dateList", dateList);
        model.addAttribute("timeSlots", timeSlots);
        
        // ★【重要】ここを追加！最初に画面を開いた時も「今の時間」を教える
        model.addAttribute("currentDateTime", LocalDateTime.now().toString());

        model.addAttribute("orderForm", new OrderForm());

        return "purchase/purchase-inf";
    }

    /**
     * お届け先入力 → 最終確認画面
     */
    /**
     * お届け先入力 → 最終確認画面
     * データ保持無視、画面遷移だけ
     */
   /* @GetMapping("/purchase/purchase-in")
    public String purchaseIn(Model model, @AuthenticationPrincipal UserDetailsImpl principal,@RequestParam Integer quantity) {
        // 空のカートリストと合計金額を設定
      
        List<CartItem> cartItems = cartService.findCartItems(principal.getUser());
        int totalPrice = quantity;

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        return "purchase/purchase-in"; // templates/purchase/purchase-in.html
    }*/
    @PostMapping("/purchase/confirm")
    public String confirmPurchase( @ModelAttribute("orderForm") OrderForm orderForm,
        BindingResult bindingResult,
        HttpSession session,
        Model model, 
        @AuthenticationPrincipal UserDetailsImpl principal
        ) {String email = principal.getUsername();
        User user = userService.findByEmail(email);
    if (bindingResult.hasErrors()) {
      //エラー時の処理
   // --- ここから追加：画面表示に必要なデータを再セット ---
      model.addAttribute("address1", user.getAddresses());
      
      List<Order> latestOrder = orderService.findOrdersByEmail(email); 
      model.addAttribute("address2", (latestOrder != null && !latestOrder.isEmpty()) ? latestOrder.get(0) : null);

      // 日付リスト（30日分）
      List<LocalDate> dateList = new ArrayList<>();
      LocalDate today = LocalDate.now();
      for (int i = 0; i < 31; i++) { dateList.add(today.plusDays(i)); }
      
   // エラー時の処理の中で時間を計算して削っている部分を、シンプルな10-22時のリストに変えます

      List<String> timeSlots = new ArrayList<>();
      for (int h = 10; h <= 22; h++) {
          timeSlots.add(String.format("%02d:00", h));
      }
      // 順番を整える
      java.util.Collections.sort(timeSlots);

      model.addAttribute("timeSlots", timeSlots);
      // JavaScriptが計算に使う「今の時間」を渡す
      model.addAttribute("currentDateTime", LocalDateTime.now().toString());

      model.addAttribute("dateList", dateList);
      model.addAttribute("timeSlots", timeSlots);
      model.addAttribute("orderForm", orderForm);

      return "purchase/purchase-inf";
    }
        // カートアイテムを取得
        List<CartItem> cartItems = cartService.findCartItems(principal.getUser());
        // 合計金額を計算
        int totalPrice = cartService.calcTotal(cartItems);
        
        // モデルにデータを追加
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orderForm", orderForm);
        
        return "purchase/purchase-in"; // templates/purchase/confirm.html
        
        
    }
    @PostMapping("/purchase/complete")
    public String completePurchase(@ModelAttribute OrderForm orderForm, Model model, @AuthenticationPrincipal
        UserDetailsImpl principal) {
        // カートアイテムを取得
        List<CartItem> cartItems = cartService.findCartItems(principal.getUser());
        
        String email = principal.getUsername();
        // 注文を確定
        orderService.createOrder(email, cartItems,orderForm);
        return "purchase/purchase-out";
    }
    /**
     * Stripe決済完了後にリダイレクトされる完了画面表示
     */
    @GetMapping("/purchase/purchase-out")
    public String showPurchaseOut(@AuthenticationPrincipal
        UserDetailsImpl principal) {
        // ここで必要であればカートを空にする処理(cartService.clearCartなど)を呼ぶのが一般的です
      List<CartItem> cartItems = cartService.findCartItems(principal.getUser());
      User user = principal.getUser();
        cartService.remove(user, cartItems.get(0).getProduct().getId());
        // templates/purchase/purchase-out.html を表示
        return "purchase/purchase-out";
    }
}

