package com.example.springlesson.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springlesson.entity.Product;
import com.example.springlesson.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 有効な商品一覧取得
     */
    @Transactional(readOnly = true)
    public List<Product> findActiveProducts() {
        return productRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }

    /**
     * 商品検索
     */
    @Transactional(readOnly = true)
    public List<Product> searchProducts(
            String keyword,
            Long categoryId,
            Integer minPrice,
            Integer maxPrice) {

        return productRepository.search(
                keyword,
                categoryId,
                minPrice,
                maxPrice
        );
    }

    /**
     * 商品詳細取得
     */
    @Transactional(readOnly = true)
    public Product findById(Long id) {
      return productRepository.findById(id.longValue())
              .orElseThrow(() ->
                      new IllegalArgumentException("商品が存在しません。"));
  }

}
