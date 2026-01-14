package com.example.springlesson.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springlesson.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

  public Category findByName(String categoryName);
}
