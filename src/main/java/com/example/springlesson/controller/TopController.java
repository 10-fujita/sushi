package com.example.springlesson.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.springlesson.entity.Review;
import com.example.springlesson.service.TopService;
// コントローラーからhtmlを呼び出す
@Controller
public class TopController {
  
 private final TopService topService;
 
 public TopController(TopService topService) {
   this.topService = topService;
   }
 
  @GetMapping("/")
  public String index(Model model) {
    //商品レビューの取得
    try {
      List<Review> voiceList = topService.getTop2Reviews();
      model.addAttribute("voiceList", voiceList);
      }catch(Exception e) {
      model.addAttribute("errMsg","エラーのため表示できません");
    }

    return "index";  // index.htmlを呼び出す
    
  }

}
