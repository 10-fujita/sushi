package com.example.springlesson.entity; // パッケージ名はご自身の環境に合わせてください

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "password_reset_tokens")
@Data // Lombokを使用している場合。ない場合はGetter/Setterを生成してください
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    // Userエンティティとの紐付け（1対1の関係）
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // デフォルトコンストラクタ（JPA用）
    public PasswordResetToken() {}

    // コンストラクタ（作成時に使うと便利）
    public PasswordResetToken(String token, User user, int expiryMinutes) {
        this.token = token;
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(expiryMinutes);
    }
    
    // 期限切れチェック用メソッド
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
}