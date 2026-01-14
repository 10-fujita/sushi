package com.example.springlesson.form;

import lombok.Data;

@Data
public class ProductSearchForm {

    /** キーワード検索 */
    private String keyword;

    /** カテゴリID */
    private Long categoryId;

    /** 最低価格 */
    private Integer minPrice;

    /** 最高価格 */
    private Integer maxPrice;
}
