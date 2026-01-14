package com.example.springlesson.form;

import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AdminAddProductForm {
@NotNull(message ="商品の名前を入力してください" )
 private  String name;;
@NotNull(message ="商品のカテゴリーを選択してください" )
  private String categoryName;
@NotNull(message ="商品の詳細を入力してください" )
  private String description;
@NotNull(message ="商品の価格を入力してください" )
  private Integer price;
@NotNull(message ="商品のカロリーを入力してください" )
  private Integer calories;
  
@NotNull(message ="商品のアレルギー情報Lを入力してください" )
  private String allergyInfo;
@NotNull(message ="商品の原材料情報を入力してください" )
  private String ingredientInfo;
@NotNull(message ="商品の画像ファイルは必須です" )
 private MultipartFile image;
}
