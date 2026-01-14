package com.example.springlesson.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springlesson.entity.CartItem;
import com.example.springlesson.entity.Product;
import com.example.springlesson.security.UserDetailsImpl;
import com.example.springlesson.service.CartService;
import com.example.springlesson.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;
    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    /**
     * 商品一覧表示
     */
    @GetMapping
    public String list(@AuthenticationPrincipal UserDetailsImpl principal, Model model) {

        List<Product> products = productService.findActiveProducts();
        model.addAttribute("products", products);
        
     // 【追加】現在のカートの中身を取得
        if (principal != null) {
            List<CartItem> cartItems = cartService.findCartItems(principal.getUser());
            // 「どの商品IDが何個か」のマップを作って渡すとJSで扱いやすい
            Map<Long, Integer> cartMap = cartItems.stream()
                .collect(Collectors.toMap(item -> item.getProduct().getId(), CartItem::getQuantity));
            model.addAttribute("currentCart", cartMap);
        }

        return "product/product";
    }

    /**
     * 商品検索
     * ・キーワード
     * ・カテゴリ
     * ・価格帯
     */
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            Model model) {

        List<Product> products = productService.searchProducts(
                keyword, categoryId, minPrice, maxPrice);

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "product/product";
    }

    /**
     * 商品詳細
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {

        Product product = productService.findById(id);
        model.addAttribute("product", product);

        return "product/detail";
    }
}
