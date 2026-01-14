package com.example.springlesson.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springlesson.entity.OrderItem;
import com.example.springlesson.entity.Review;
import com.example.springlesson.entity.User;
import com.example.springlesson.form.ReviewForm;
import com.example.springlesson.repository.OrderItemRepository;
import com.example.springlesson.repository.ProductRepository;
import com.example.springlesson.repository.ReviewRepository;

@Service
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final OrderItemRepository orderItemRepository;
  private final ProductRepository productRepository;
  public ReviewService(ReviewRepository reviewRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
    this.reviewRepository = reviewRepository;
    this.orderItemRepository = orderItemRepository;
    this.productRepository = productRepository;
  }
  
 @Transactional
 public List<Review> getLatest5Reviews() {
   return reviewRepository.findTop5ByOrderByCreatedAtDesc();
 }
 
 @Transactional
 public void createReview(User loginUser, ReviewForm form) {
   Review review = new Review();
   // review.setReviewerName(form.getReviewerName());
   // review.setTitle(form.getTitle());
   review.setComment(form.getComment());
   review.setUser(loginUser);
   review.setRating(3); // 仮の評価値
   review.setProduct(productRepository.findById(form.getProductId())
       .orElseThrow(() -> new RuntimeException("商品が見つかりませんでした")));
   
   reviewRepository.save(review);
 }
 @Transactional
 public List<OrderItem> getOrderItemsByUserId(Long userId) {
   return orderItemRepository.findByOrder_User_Id(userId);
 }
}

