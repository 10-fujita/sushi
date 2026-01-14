package com.example.springlesson.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springlesson.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  public Role findByName(String name);
}