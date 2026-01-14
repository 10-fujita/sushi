package com.example.springlesson.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springlesson.entity.CartItem;
import com.example.springlesson.entity.Product;
import com.example.springlesson.entity.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // ユーザーごとのカートアイテム一覧を取得
    List<CartItem> findByUser(User user);

    // ユーザー＋商品でカートアイテムを取得
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    
    Optional<CartItem> findByUserId(Long userId);
   
    CartItem deleteByUserId(Long userId);
}
