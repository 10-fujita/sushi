package com.example.springlesson.controller;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springlesson.entity.OrderItem;
import com.example.springlesson.entity.Product;
import com.example.springlesson.entity.Review;
import com.example.springlesson.form.ReviewForm;
import com.example.springlesson.security.UserDetailsImpl;
import com.example.springlesson.service.ReviewService;


@Controller
@RequestMapping("/review")
public class ReviewController {
  private final ReviewService reviewService;

  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  // レビュー一覧ページ
  @GetMapping
  public String list(Model model) {
    try {
      List<Review> reviewList = reviewService.getLatest5Reviews();
      model.addAttribute("reviews", reviewList);
    } catch (DataAccessException e) {
      // 本番ではログ出力してエラーページへ飛ばすのが一般的です
      return "error/error"; 
    }
    // "review/reviewList" から "reviewList" に修正（フォルダがない場合）
    return "review/reviewList"; 
  }

  // レビュー投稿ページ
  @GetMapping("/new")
  public String newReview(Model model,@AuthenticationPrincipal UserDetailsImpl userDetails) {

    Long userId = userDetails.getUser().getId();
    try {
      // ユーザーが購入した商品を取得
    } catch (DataAccessException e) {
      return "error/error";
    }
    List<OrderItem> orderItems = reviewService.getOrderItemsByUserId(userId);
 // Productだけを重複なしで取り出す
    List<Product> purchasedProducts = orderItems.stream()
        .map(OrderItem::getProduct)
        .distinct()
        .toList();
    model.addAttribute("purchasedProducts", purchasedProducts);
    model.addAttribute("reviewForm", new ReviewForm());
    return "review/reviewForm";
  }

  @PostMapping("/new")
  public String createReview(@AuthenticationPrincipal UserDetailsImpl principal,
      @ModelAttribute ReviewForm reviewForm) {
    
    // ★重要：ログインチェック（これがないと未ログイン時に落ちます）
    if (principal == null) {
      return "redirect:/auth/login";
    }

    try {
      reviewService.createReview(principal.getUser(), reviewForm);
      return "redirect:/review";
    } catch (DataAccessException e) {
      // エラー画面に飛ばすのではなく、入力画面に戻してメッセージを出すのが理想
      return "review/reviewForm";
    }
  }
}