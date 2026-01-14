package com.example.springlesson.dto;

import java.text.NumberFormat;

import lombok.Data;

@Data
public class ProductDTO {
  // 商品ID
  private Integer id;

  // 商品名
  private String name;
  
  // 価格
  private Integer price;

  /** コンストラクター */
  // 引数なし
  public ProductDTO() {}
  // 引数あり
  public ProductDTO(Integer id, String name, Integer price) {
    this.id = id;
    this.name = name;
    this.price = price;
  } 
  
  
  /** 表示用メソッド */
  // 桁区切り(Thymeleaf3以上からThymeleafのみでは桁区切り不可になっている)
  public String getFormattedPrice() {
    return NumberFormat.getNumberInstance().format(this.price);
  }
}
