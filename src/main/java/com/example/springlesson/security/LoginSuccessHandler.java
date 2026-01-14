package com.example.springlesson.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.example.springlesson.entity.User;
import com.example.springlesson.repository.UserRepository;

public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final UserRepository userRepository;

  public LoginSuccessHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

@Override
public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication)
        throws IOException, ServletException {
  try {
    // ログイン成功時セッションスコープからユーザー情報取得
  UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
  User user = userDetails.getUser();
  // 最終ログイン日時更新
  user.updateLastLogin();
 request.getSession().setAttribute("userInf", user);
  // DBに保存
  userRepository.save(user);
  super.onAuthenticationSuccess(request, response, authentication);
  }catch(IOException | ServletException e) {
    //例外文言の表示
    e.printStackTrace();
    request.getSession().setAttribute("errMsg", "ログイン後処理でエラーが発生しました。");
    
    //任意のページにリダイレクト
    response.sendRedirect("/error/error");
  }
 
}
}