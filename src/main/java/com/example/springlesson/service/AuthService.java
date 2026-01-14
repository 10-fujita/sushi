package com.example.springlesson.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springlesson.entity.Role;
import com.example.springlesson.entity.User;
import com.example.springlesson.entity.UserAddress;
import com.example.springlesson.form.RegistForm;
import com.example.springlesson.repository.RoleRepository;
import com.example.springlesson.repository.UserAddressRepository;
import com.example.springlesson.repository.UserRepository;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final UserAddressRepository userAddressRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;
  
  public AuthService(
      UserRepository userRepository,
      UserAddressRepository userAddressRepository,
      PasswordEncoder passwordEncoder,
      RoleRepository roleRepository
      ) {
    this.userRepository = userRepository;
    this.userAddressRepository = userAddressRepository;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
  }
  @Transactional
  public void SaveUser(RegistForm form) {
    User user =new User();
    user.setEmail(form.getEmail());
    user.setPassword(passwordEncoder.encode(form.getPassword()));
    user.setName(form.getName());
    user.setPhoneNumber(form.getPhoneNumber());
    user.setDmOptIn(form.isDmOptIn());
    
    UserAddress userAddress = new UserAddress();
    userAddress.setUser(user);
    userAddress.setRecipientName(form.getName());
    userAddress.setPostalCode(form.getPostalCode());
    userAddress.setPrefecture(form.getPrefecture());
    userAddress.setCity(form.getCity());
    userAddress.setAddressLine1(form.getAddressLine1());
    userAddress.setAddressLine2(form.getAddressLine2());
    userAddress.setIsDefault(true);
    
    Role role = roleRepository.findByName("ROLE_USER");
    user.setRole(role);
    //登録処理
    user.updateLastLogin();
    userRepository.save(user); 
    userAddressRepository.save(userAddress);
    
  }
}
