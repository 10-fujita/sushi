package com.example.springlesson.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springlesson.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order>findFirstByUserEmailOrderByOrderDatetimeDesc(String email);
    List<Order>findAll();
    List<Order>findByIdIn(List<Long>orderIds);
 // 配達日が近い順 ＋ 時間が早い順
    List<Order> findAllByOrderByDeliveryDateAscDeliveryTimeSlotAsc();
    List<Order> findByUserEmailOrderByOrderDatetimeDesc(String email);
}
