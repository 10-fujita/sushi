package com.example.springlesson.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OrderForm {

    @NotBlank(message = "宛先名を入力してください。")
    private String recipientName;

    @NotBlank(message = "郵便番号を入力してください。")
    private String postalCode;

    @NotBlank(message = "都道府県を入力してください。")
    private String prefecture;

    @NotBlank(message = "市区町村を入力してください。")
    private String city;

    @NotBlank(message = "番地を入力してください。")
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "送り先の電話番号を入力してください。")
    private String phoneNumber;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    @NotBlank
    private String deliveryTimeSlot;
 
    private String addressSelect; // ラジオボタンの値 (1, 2, 3) を受け取る
}
