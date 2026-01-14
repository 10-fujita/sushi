package com.example.springlesson.form;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AdminProductForm {
@NotNull(message = "商品が選択されていません。")
 private List<Long> productIds;

  public List<Long> getProductIds() {
    return productIds;
  }
  public void setProductIds(List<Long> productIds) {
    this.productIds = productIds;
 }
}
