package com.example.springlesson.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springlesson.entity.CartItem;
import com.example.springlesson.entity.Product;
import com.example.springlesson.entity.User;
import com.example.springlesson.repository.CartItemRepository;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final UserService userService;

    public CartService(CartItemRepository cartItemRepository, ProductService productService, UserService userService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.userService = userService;
    }

    // ユーザーごとのカートアイテム一覧
    public List<CartItem> findCartItems(User user) {
      List<CartItem> allItems = cartItemRepository.findByUser(user);
      
      // 商品が null でなく、かつ有効(enabled=true)なものだけを抽出
      return allItems.stream()
              .filter(item -> item.getProduct() != null && item.getProduct().getIsActive()) 
              .toList();
  }

    // メールからユーザーを取得してカートアイテム一覧
    public List<CartItem> getCartItemsByUserEmail(String email) {
        User user = userService.findByEmail(email);
        return findCartItems(user);
    }

    // カートに追加
    public void add(User user, Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) return;

        Product product = productService.findById(productId);

        CartItem item = cartItemRepository.findByUserAndProduct(user, product).orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(product.getPrice());
            cartItemRepository.save(newItem);
        }
    }

    // カート数量更新
 // カート数量更新（上書き・削除・新規追加をすべてカバー）
    public void updateQuantity(User user, Long productId, Integer quantity) {
        Product product = productService.findById(productId);

        // まず、DBに既存のデータがあるか探す
        CartItem item = cartItemRepository.findByUserAndProduct(user, product).orElse(null);

        if (item != null) {
            // --- ケース1：既にカートにある場合 ---
            if (quantity <= 0) {
                cartItemRepository.delete(item); // 0なら削除
            } else {
                item.setQuantity(quantity); // 1以上ならその数で上書き（保存は@Transactionalで自動で行われます）
            }
        } else if (quantity > 0) {
            // --- ケース2：まだカートにない商品を新しく追加する場合 ---
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(product.getPrice());
            cartItemRepository.save(newItem);
        }
    }

    // カートアイテム削除
    public void remove(User user, Long productId) {
        Product product = productService.findById(productId);

        cartItemRepository.findByUserAndProduct(user, product).ifPresent(cartItemRepository::delete);
    }

    // 合計金額計算
    public int calcTotal(List<CartItem> items) {
        return items.stream().mapToInt(i -> i.getUnitPrice() * i.getQuantity()).sum();
    }
    @Transactional
    public void clearCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(items);
    }
}
