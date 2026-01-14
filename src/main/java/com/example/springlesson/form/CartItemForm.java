package com.example.springlesson.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CartItemForm {
  // 商品ID
  @NotNull
  private Integer productId;
  
  // 数量
  @NotNull
  @Min(1)
  private Integer quantity;

}
