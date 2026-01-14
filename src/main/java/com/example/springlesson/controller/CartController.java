package com.example.springlesson.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springlesson.entity.CartItem;
import com.example.springlesson.entity.User;
import com.example.springlesson.security.UserDetailsImpl;
import com.example.springlesson.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

  private final CartService cartService;

  public CartController(CartService cartService) {
    this.cartService = cartService;
  }
  // カート一覧
  @GetMapping
  public String cart(@AuthenticationPrincipal UserDetailsImpl principal,
      Model model) {

    User user = principal.getUser();
    List<CartItem> cartItems = cartService.findCartItems(user);

    model.addAttribute("cartItems", cartItems);
    model.addAttribute("total", cartService.calcTotal(cartItems));

    return "cart/cart";
  }
  // カート追加
  @PostMapping("/add")
  public String add(@AuthenticationPrincipal UserDetailsImpl principal,
      @RequestParam("productIds") List<Long> productIds, 
      @RequestParam("quantities") List<Integer> quantities) {

      // 選択された商品リストをループして、一つずつサービスで追加処理を行う
      for (int i = 0; i < productIds.size(); i++) {
          Long productId = productIds.get(i);
          Integer quantity = quantities.get(i);
          
          // 数量が0より大きい場合のみカートに追加
          cartService.updateQuantity(principal.getUser(), productId, quantity);
      }

      return "redirect:/cart";
  }

  // 数量変更
  @PostMapping("/updatedAt")
  public String update(@AuthenticationPrincipal UserDetailsImpl principal,
      @RequestParam Long productId,
      @RequestParam Integer quantity) {

    cartService.updateQuantity(principal.getUser(), productId, quantity);
    return "redirect:/cart";
  }
  // カート削除
  @PostMapping("/delete")
  public String remove(@AuthenticationPrincipal UserDetailsImpl principal,
      @RequestParam Long productId) {

    cartService.remove(principal.getUser(), productId);
    return "redirect:/cart";
  }

}
