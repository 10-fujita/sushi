package com.example.springlesson.repository;




import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springlesson.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
public List<User> findByIdIn(List<Long> userId);
public Optional<User> findByEmail(String email);

}
