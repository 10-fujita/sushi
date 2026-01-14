package com.example.springlesson.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springlesson.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  List<OrderItem> findByOrder_User_Id(Long userId);
  List<OrderItem>findAll();
  List<OrderItem>save(String order);
}
