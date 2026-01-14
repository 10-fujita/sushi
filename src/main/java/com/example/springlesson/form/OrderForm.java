package com.example.springlesson.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class OrderForm {

    @NotBlank
    private String recipientName;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String prefecture;

    @NotBlank
    private String city;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @NotBlank
    private String phoneNumber;

    @NotNull
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    @NotBlank
    private String deliveryTimeSlot;
 
    private String addressSelect; // ラジオボタンの値 (1, 2, 3) を受け取る
}
