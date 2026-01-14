package com.example.springlesson.form;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class AdminOrderForm {

  @NotEmpty(message = "注文を1つ以上選択してください。")
  private List<Long> orderIds;
}
