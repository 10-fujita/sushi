package com.example.springlesson.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springlesson.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 有効商品のみ取得
     */
    List<Product> findByIsActiveTrueOrderByCreatedAtDesc();

    /**
     * 商品検索（複合条件）
     */
    @Query("""
        SELECT DISTINCT p
        FROM Product p
      
        LEFT JOIN p.category c
        WHERE p.isActive = true
          AND (:keyword IS NULL OR p.name LIKE %:keyword%)
          AND (:categoryId IS NULL OR c.id = :categoryId)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        ORDER BY p.createdAt DESC
    """)
    List<Product> search(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
    );
    public List<Product> findByIdIn(List<Long> productIds);
    
}
