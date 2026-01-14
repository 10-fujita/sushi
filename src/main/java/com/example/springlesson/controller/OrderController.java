package com.example.springlesson.controller;

import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springlesson.entity.CartItem;
import com.example.springlesson.form.OrderForm;
import com.example.springlesson.service.CartService;
import com.example.springlesson.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping
    public String input(Model model, Principal principal) {
        List<CartItem> cart = cartService.findCartItems(
                cartService.findCartItems(null).get(0).getUser());

        model.addAttribute("cart", cart);
        model.addAttribute("orderForm", new OrderForm());
        return "order/order-input";
    }

    @PostMapping("/confirm")
    public String confirm(
            @Valid @ModelAttribute OrderForm form,
            BindingResult result,
            Principal principal,
            Model model) {

        if (result.hasErrors()) {
            return "order/order-input";
        }

        orderService.createOrder(
                principal.getName(),
                cartService.findCartItems(
                        cartService.findCartItems(null).get(0).getUser()),
                form);

        return "order/order-complete";
    }

    @GetMapping("/history")
    public String history(Principal principal, Model model) {
        model.addAttribute("orders",
                orderService.findOrdersByEmail(principal.getName()));
        return "order/order-history";
    }
}
