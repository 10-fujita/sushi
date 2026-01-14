package com.example.springlesson.form;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AdminCustomerForm {

 @NotNull(message = "ユーザーが選択されていません。")
 private List<Long> userIds;

  public List<Long> getUserIds() {
    return userIds;
  }
  public void setUserIds(List<Long> userIds) {
    this.userIds = userIds;
 }
 
 
}
