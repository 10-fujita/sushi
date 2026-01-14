package com.example.springlesson.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springlesson.entity.User;
import com.example.springlesson.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;
  
  // コンストラクターインジェクション
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }



  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    try {
      User user = userRepository.findByEmail(email).orElse(null);
      if(user == null) {
        throw new UsernameNotFoundException("ユーザーが見つかりません：" + email);
      }
      // 権限(とりあえずここに固定しておきます)
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      if(user.getRole() != null) {
          authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
      }
      return new UserDetailsImpl(user, authorities);
    }catch(Exception e) {
      throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
    }
  }
  
}
