package com.example.springlesson.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springlesson.entity.Category;
import com.example.springlesson.entity.Order;
import com.example.springlesson.entity.Product;
import com.example.springlesson.entity.User;
import com.example.springlesson.form.AdminAddProductForm;
import com.example.springlesson.repository.CategoryRepository;
import com.example.springlesson.repository.OrderRepository;
import com.example.springlesson.repository.ProductRepository;
import com.example.springlesson.repository.UserRepository;

@Service
public class AdminService {
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final OrderRepository orderRepository;

  public AdminService(
      UserRepository userRepository,
      ProductRepository productRepository,
      CategoryRepository categoryRepository,
      OrderRepository orderRepository) {
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.orderRepository = orderRepository;

  }

  @Transactional
  public void disableUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。ID: " + userId));

    user.setWithdrawFlag(true);
  }

  @Transactional
  public void disableProduct(Long productId) {
    Product Product = productRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("商品が見つかりません。ID: " + productId));
    Product.setIsActive(false);
  }

  @Transactional
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  @Transactional
  public List<Product> findAllProducts() {
    return productRepository.findAll();
  }

  @Transactional
  public List<User> findByIdIn(List<Long> userIds) {
    return userRepository.findByIdIn(userIds);
  }

  @Transactional
  public List<Product> findByIdInProducts(List<Long> productIds) {
    return productRepository.findByIdIn(productIds);
  }

  @Transactional
  public Product saveProduct(AdminAddProductForm form) {
    Product product = new Product();
    product.setName(form.getName());
    product.setDescription(form.getDescription());
    product.setPrice(form.getPrice());
    product.setAllergyInfo(form.getAllergyInfo());
    product.setIsActive(true); // 新規商品は有効に設定
    product.setIngredientInfo(form.getIngredientInfo());
    String searchName = "";

    // 1. IF文でHTMLの値からDBの検索用文字列へ変換
    if ("nigiri".equals(form.getCategoryName())) {
      searchName = "にぎり";
    } else if ("gunnkann".equals(form.getCategoryName())) {
      searchName = "軍艦";
    } else if ("etc".equals(form.getCategoryName())) {
      searchName = "その他";
    }
    Category category = categoryRepository.findByName(searchName);
    product.setCategory(category);

    // 商品を保存
    return productRepository.save(product);
  }

  @Transactional
  public Product updateProduct(Product product) {
    Category category = categoryRepository.findByName(product.getCategory().getName());
    product.setCategory(category);

    // 商品を保存
    return productRepository.save(product);
  }

  @Transactional
  public List<Order> findAllOrders() {
    return orderRepository.findAllByOrderByDeliveryDateAscDeliveryTimeSlotAsc();
  }

  @Transactional
  public List<Order> findByOrderId(List<Long> orderIds) {
    
    return orderRepository.findByIdIn(orderIds);
  }
  
@Transactional
  public List<Order>save(List<Long> orderIds){ {
 
 // 1. まずデータベースから対象のOrderエンティティをすべて取得する
    List<Order> orders = orderRepository.findByIdIn(orderIds);

    // 2. 取得した各エンティティのstatusを書き換える
    for (Order order : orders) {
        order.setStatus("COMPLETED");
        
}return orderRepository.saveAll(orders);
}  
}
}