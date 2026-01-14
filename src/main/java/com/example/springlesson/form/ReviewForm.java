package com.example.springlesson.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class ReviewForm {
  @NotBlank(message = "投稿者名は必須です")
  @Size(max = 50, message = "投稿者名は50文字以内で入力してください")
  private String reviewerName;

  @NotBlank(message = "投稿タイトルは必須です")
  @Size(max = 100, message = "投稿タイトルは100文字以内で入力してください")
  private String title;
  
  @NotBlank(message = "感想・コメントは必須です")
  @Size(max = 1000, message = "感想・コメントは1000文字以内で入力してください")
  private String comment;
  
  @NotBlank(message = "商品を選択してください")
  private Long productId;
}
