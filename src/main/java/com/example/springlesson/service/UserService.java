package com.example.springlesson.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springlesson.entity.Order;
import com.example.springlesson.entity.User;
import com.example.springlesson.entity.UserAddress;
import com.example.springlesson.form.UserUpdateForm;
import com.example.springlesson.repository.OrderRepository;
import com.example.springlesson.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public UserService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
      User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new IllegalStateException("ユーザーが存在しません");
        }
        return user;
    }


    @Transactional
    public void updateUser(String email, UserUpdateForm form) {
        User user = findByEmail(email);
        String name =user.getAddresses().get(0).getRecipientName();

        user.setName(form.getName());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setEmail(form.getEmail());
        UserAddress useradress = new UserAddress();
        useradress.setUser(user);
         useradress.setPostalCode(form.getPostalCode());
         useradress.setPrefecture(form.getPrefecture());
         useradress.setCity(form.getCity());
         useradress.setAddressLine1(form.getAddressLine1());
         useradress.setAddressLine2(form.getAddressLine2());
         useradress.setRecipientName(name);
         useradress.setIsDefault(false);
         user.getAddresses().add(useradress);
         
        

        userRepository.save(user);
    }
    //購入履歴一覧
    @Transactional
    public List<Order> getUserWithOrders(String email) {
       

        return  orderRepository.findByUserEmailOrderByOrderDatetimeDesc(email);
    }

    /** 退会処理（論理削除） */
    @Transactional
    public void withdraw(String email) {
        User user = findByEmail(email);
        user.setWithdrawFlag(true);
        userRepository.save(user);
    }
}

