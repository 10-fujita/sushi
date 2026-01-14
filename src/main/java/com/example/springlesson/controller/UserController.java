package com.example.springlesson.controller;

import java.security.Principal;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springlesson.entity.Order;
import com.example.springlesson.entity.User;
import com.example.springlesson.entity.UserAddress;
import com.example.springlesson.form.UserUpdateForm;
import com.example.springlesson.service.UserService;

@Controller
@RequestMapping("/mypage")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** マイページ表示 */
    @GetMapping
    public String mypage(Principal principal, Model model) {
        model.addAttribute("user", userService.findByEmail(principal.getName()));
        return "mypage/mypage";
    }

    /** 会員情報編集画面（editpro.html） */
    @GetMapping("/editpro")
    public String editPro(Principal principal, Model model) {
      User user = userService.findByEmail(principal.getName());
      UserUpdateForm form = new UserUpdateForm();
      form.setName(user.getName());
      form.setPhoneNumber(user.getPhoneNumber());
      form.setEmail(user.getEmail());
      model.addAttribute("userUpdateForm", form);
      
      List<UserAddress> addresses = user.getAddresses();
      model.addAttribute("addressList", addresses);
        return "mypage/editpro"; 
    }

    /** 確認画面 */
    @PostMapping("/editpro")
    public String update(
            @Valid @ModelAttribute("userUpdateForm") UserUpdateForm form,
            BindingResult result,
            Principal principal,
            HttpSession session,
            Model model) {

        if (result.hasErrors()) {
            return "mypage/editpro"; 
        }
        model.addAttribute("userUpdateForm", form);
        session.setAttribute("userUpdateForm", form);
        return "mypage/checkEd";
    }
    /** 更新確認画面からの確定処理 */
    @PostMapping("/save")
    public String saveUpdate(HttpSession session, Principal principal) {
        UserUpdateForm form = (UserUpdateForm) session.getAttribute("userUpdateForm");
        userService.updateUser(principal.getName(), form);
        session.removeAttribute("userUpdateForm");
        return "redirect:/mypage";
    }
    //購入履歴確認処理
    @GetMapping("/history")
    public String viewPurchaseHistory(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
       List<Order> orders = userService.getUserWithOrders(user.getEmail());
         model.addAttribute("orders", orders);
      return "mypage/history";
    }
    /** 退会 */
    @PostMapping("/withdraw")
    public String withdraw(Principal principal) {
        userService.withdraw(principal.getName());
        return "login/login";
    }
}
