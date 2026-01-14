package com.example.springlesson.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springlesson.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
// 直近5件の口コミを作成日時の降順で取得するメソッド
 List<Review> findTop5ByOrderByCreatedAtDesc();
//直近2件の口コミを作成日時の降順で取得するメソッド（Top用）
 List<Review> findTop2ByOrderByIdAsc();
}
