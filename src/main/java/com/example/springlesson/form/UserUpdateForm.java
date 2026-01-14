package com.example.springlesson.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class UserUpdateForm {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 20)
    private String phoneNumber;

    @NotBlank(message = "メールアドレスを入力してください。")
    @Email(message = "有効なメールアドレスを入力してください。")
    @Size(max =255, message="メールアドレスは255文字以内で入力してください。")
    private String email;
    
  
    private String postalCode;
    private String prefecture;
    private String city;
    private String addressLine1;
    private String addressLine2;
}
