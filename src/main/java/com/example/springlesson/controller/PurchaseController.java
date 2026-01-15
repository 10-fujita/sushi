package com.example.springlesson.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

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
     // 新しい注文フォーム（紙）を作る
        OrderForm orderForm = new OrderForm();
        orderForm.setAddressSelect("1"); // デフォルトで「会員登録住所」を選択
        model.addAttribute("orderForm",orderForm);

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
    public String confirmPurchase(@Valid @ModelAttribute("orderForm") OrderForm orderForm,
        BindingResult bindingResult,
        HttpSession session,
        Model model, 
        @AuthenticationPrincipal UserDetailsImpl principal
    ) {
        String email = principal.getUsername();
        User user = userService.findByEmail(email);

        // --- ★ここから追加：選ばれた住所を orderForm にセットする ---
        
        // 1. 「会員登録住所」が選ばれた場合
        if ("1".equals(orderForm.getAddressSelect())) {
            List<UserAddress> addresses = user.getAddresses();
            if (addresses != null && !addresses.isEmpty()) {
                // 現在のプルダウン選択に関わらず、まずは1番目の住所をセットする場合
                UserAddress addr = addresses.get(0); 
                orderForm.setPostalCode(addr.getPostalCode());
                orderForm.setPrefecture(addr.getPrefecture());
                orderForm.setCity(addr.getCity());
                orderForm.setAddressLine1(addr.getAddressLine1());
                orderForm.setAddressLine2(addr.getAddressLine2());
            }
        } 
        // 2. 「前回利用した住所」が選ばれた場合
        else if ("2".equals(orderForm.getAddressSelect())) {
            List<Order> latestOrders = orderService.findOrdersByEmail(email);
            if (latestOrders != null && !latestOrders.isEmpty()) {
                Order last = latestOrders.get(0);
                orderForm.setPostalCode(last.getShippingPostalCode());
                orderForm.setPrefecture(last.getShippingPrefecture());
                orderForm.setCity(last.getShippingCity());
                orderForm.setAddressLine1(last.getShippingAddressLine1());
                orderForm.setAddressLine2(last.getShippingAddressLine2());
            }
        }
        // ※「3. 新しく入力する」の場合は、すでに入力欄から値が入っているのでそのままでOK
        
        // --- ★追加ここまで ---

        // バリデーションエラーがある場合の処理（既存のまま）
        if (bindingResult.hasErrors()) {
            model.addAttribute("address1", user.getAddresses());
            List<Order> latestOrder = orderService.findOrdersByEmail(email); 
            model.addAttribute("address2", (latestOrder != null && !latestOrder.isEmpty()) ? latestOrder.get(0) : null);

            List<LocalDate> dateList = new ArrayList<>();
            LocalDate today = LocalDate.now();
            for (int i = 0; i < 31; i++) { dateList.add(today.plusDays(i)); }
            
            List<String> timeSlots = new ArrayList<>();
            for (int h = 10; h <= 22; h++) { timeSlots.add(String.format("%02d:00", h)); }
            
            model.addAttribute("dateList", dateList);
            model.addAttribute("timeSlots", timeSlots);
            model.addAttribute("currentDateTime", LocalDateTime.now().toString());
            model.addAttribute("orderForm", orderForm);
            return "purchase/purchase-inf";
        }

        // カート情報の取得（既存のまま）
        List<CartItem> cartItems = cartService.findCartItems(user);
        int totalPrice = cartService.calcTotal(cartItems);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orderForm", orderForm); // 住所を詰め直した orderForm を渡す
        
        return "purchase/purchase-in";
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

