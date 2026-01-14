package com.example.springlesson.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity  // DBからのデータ格納用クラスであることを表す
@Table(name="users")
@Data  // getter/setterを自動で作成してくれる
@NoArgsConstructor  // 引数なしのコンストラクターをlombokが作成してくれる
public class User {
  @Id  // 主キーを表す
  @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENTの場合付与が必要
  @Column(name="id")  // DB上のカラム名と変数名を別にしたい場合は必須
  private long id;
  
  @Column(name="email", nullable =false)  // DB上のカラム名と変数名を別にしたい場合は必須
  private String email;
  
  @Column(name="password_hash", nullable =false)  // DB上のカラム名と変数名を別にしたい場合は必須
  private String password;
  
  @Column(name="name", nullable =false)  // DB上のカラム名と変数名を別にしたい場合は必須
  private String name;
  
  @Column(name="phone_number")  // DB上のカラム名と変数名を別にしたい場合は必須
  private String phoneNumber;
  
  @Column(name="dm_opt_in", nullable =false)  // DB上のカラム名と変数名を別にしたい場合は必須
  private boolean dmOptIn; ;
  
  @Column(name="withdraw_flag", nullable =false)  // DB上のカラム名と変数名を別にしたい場合は必須
  private boolean withdrawFlag;
  
  @Column(name="last_login_at")  // DB上のカラム名と変数名を別にしたい場合は必須
  private LocalDateTime lastLoginAt;
  
  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  private LocalDateTime updatedAt;
  
  @OneToMany(mappedBy = "user", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
  private List<UserAddress> addresses;
  
  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  // createdAt, updatedAt はDB側で自動設定するため、アプリケーション側では設定しない

  // lastLoginAt はログイン時に別で更新する
  public void updateLastLogin() {
      this.lastLoginAt = LocalDateTime.now();


  }
}
