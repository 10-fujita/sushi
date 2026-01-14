package com.example.springlesson.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springlesson.entity.PasswordResetToken;
import com.example.springlesson.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    // トークン文字列で検索するためのメソッド
    Optional<PasswordResetToken> findByToken(String token);
    
    // ユーザーごとに古いトークンがあれば消すために用意（任意）
    void deleteByUser(User user);
}