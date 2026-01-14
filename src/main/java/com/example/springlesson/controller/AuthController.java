package com.example.springlesson.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springlesson.form.RegistForm;
import com.example.springlesson.service.AuthService;
import com.example.springlesson.service.PasswordResetService;

@Controller
@RequestMapping("/auth")
public class AuthController {
  
  private final AuthService authService;
  private PasswordResetService passwordResetService;

public AuthController(AuthService authService, PasswordResetService passwordResetService) {
    this.authService = authService;
    this.passwordResetService = passwordResetService;
  }

@GetMapping("/login")
    public String login() {
  
        return "login/login";
    }
  @GetMapping("/sign-up")
    public String signUp( Model model) {
    model.addAttribute("registForm", new RegistForm());
        return "signup/signup";
    }
  @PostMapping("/sign-up")
  public String signUpPost( @Valid @ModelAttribute("registForm") RegistForm form,
      BindingResult bindingResult,
      HttpSession session,
      Model model
      ) {
  if (bindingResult.hasErrors()) {
    //エラー時の処理
    return "signup/signup";
  }
  
  session.setAttribute("registForm", form);
  return "signup/checkSi";
  }
  @GetMapping("/correct")
  public String correct(HttpSession session, Model model) {
  RegistForm form = (RegistForm) session.getAttribute("registForm");
  model.addAttribute("registForm", form);
      return "signup/signup";
  }
  @PostMapping("/save")
  public String signUpComplete( HttpSession session,
      Model model
      ) {
    //セッションスコープより登録内容取得
    RegistForm form = (RegistForm) session.getAttribute("registForm");
    
    try {

   authService.SaveUser(form);
   return "login/login";
    } catch (Exception e) {
      //例外発生時の処理
      e.printStackTrace(); 
      model.addAttribute("errMsg", "ユーザー登録に失敗しました。");
      return "error/error";
    }
       
    }
  @GetMapping("/re-pass")
  public String rePass() {
     
      return "login/re-pass";
  }
  @PostMapping("/re-pass")
  public String rePassPost(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
    try {
      //パスワード再設定処理
      passwordResetService.createAndSendToken(email);
      redirectAttributes.addFlashAttribute("successMessage", "再設定用のメールを送信しました。");
     
    } catch (Exception e) {
      //例外発生時の処理
      e.printStackTrace(); 
      redirectAttributes.addFlashAttribute("errorMessage", "メールの送信に失敗しました。アドレスを確認してください。");
    }
    return "redirect:/auth/re-pass";
  }
  /**
   * 1. メールリンククリック時の処理
   * パスワード再設定画面を表示する
   */
  @GetMapping("/reset-password")
  public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
      // トークンの有効性をチェック（Serviceに後述のvalidateメソッドが必要）
      boolean isValid = passwordResetService.validatePasswordResetToken(token);
      
      if (!isValid) {
          model.addAttribute("errorMessage", "無効なトークンか、期限が切れています。");
          return "login/re-pass"; // 再度メール送信画面へ
      }
      
      // トークンを画面（HTML）に引き継ぐ
      model.addAttribute("token", token);
      return "login/reset-password-form"; // 新しいパスワード入力画面（HTML）
  }

  /**
   * 2. 新しいパスワードの保存処理
   */
  @PostMapping("/reset-password")
  public String handlePasswordReset(@RequestParam("token") String token, 
                                    @RequestParam("password") String password, 
                                    RedirectAttributes redirectAttributes) {
      try {
          // パスワード更新処理（Serviceに後述のupdatePasswordメソッドが必要）
          passwordResetService.updatePassword(token, password);
          redirectAttributes.addFlashAttribute("successMessage", "パスワードを更新しました。新しいパスワードでログインしてください。");
          return "redirect:/auth/login";
      } catch (Exception e) {
          redirectAttributes.addFlashAttribute("errorMessage", "エラーが発生しました。もう一度やり直してください。");
          return "redirect:/auth/re-pass";
      }
  }
  }
