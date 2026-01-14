package com.example.springlesson.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @Column(name = "order_datetime")
    private LocalDateTime orderDatetime;

    private String status;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "shipping_recipient_name")
    private String shippingRecipientName;

    @Column(name = "shipping_postal_code")
    private String shippingPostalCode;

    private String shippingPrefecture;
    private String shippingCity;
    private String shippingAddressLine1;
    private String shippingAddressLine2;
    private String shippingPhoneNumber;

    private LocalDate deliveryDate;
    private String deliveryTimeSlot;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;
}
