package com.example.springlesson.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springlesson.entity.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
  public UserAddress save(UserAddress userAddress);
  
}
