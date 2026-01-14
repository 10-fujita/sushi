package com.example.springlesson.form;

import com.example.springlesson.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // 引数なしのコンストラクターをlombokが作成してくれる
@AllArgsConstructor // 全フィールドを使用したコンストラクターをlombokが作成してくれる
public class Item {
  // 商品情報
  private ProductDTO product;
  // 個数
  private int count;
}
