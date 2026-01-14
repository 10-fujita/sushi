package com.example.springlesson.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegistForm {
  // 名前
  @NotBlank(message = "名前を入力してください。")
  @Size(max =100, message="名前は100文字以内で入力してください。")
  private String name;
  // メールアドレス
  @NotBlank(message = "メールアドレスを入力してください。")
  @Email(message = "有効なメールアドレスを入力してください。")
  @Size(max =255, message="メールアドレスは255文字以内で入力してください。")
  private String email;
  // パスワード
  @NotBlank(message = "パスワードを入力してください。")
  @Size(min =4 ,max =255, message="パスワードは4文字以上255文字以内で入力してください。")
  private String password;
  // 電話番号
  @Size(max =20, message="電話番号は20文字以内で入力してください。")
  private String phoneNumber;
  //郵便番号
  @NotBlank(message = "郵便番号を入力してください。")
  @Pattern(regexp = "\\d{7}", message = "郵便番号は7桁の数字で入力してください")
  private String postalCode;
  // 都道府県
  @NotBlank(message = "都道府県を入力してください。")
  private String prefecture;
  // 市区町村
  @NotBlank(message = "市区町村を入力してください。")
  private String city;
  // 住所1
  @NotBlank(message = "住所1を入力してください。")
  private String addressLine1;
  // 住所2
  private String addressLine2;
  //DM通知
  private boolean dmOptIn;
}
