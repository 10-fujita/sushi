package com.example.springlesson.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springlesson.entity.PasswordResetToken;
import com.example.springlesson.entity.User;
import com.example.springlesson.repository.PasswordResetTokenRepository;
import com.example.springlesson.repository.UserRepository;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * パスワード再設定メールを送信する
     */
    @Transactional
    public void createAndSendToken(String email) throws Exception {
        // 1. ユーザーの存在確認
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("ユーザーが見つかりません"));

        // 2. 既存の古いトークンがあれば削除（1人1つにするため）
        tokenRepository.deleteByUser(user);

        // 3. ランダムなトークンを生成（UUID）
        String token = UUID.randomUUID().toString();

        // 4. 有効期限を30分に設定して保存
        PasswordResetToken resetToken = new PasswordResetToken(token, user, 30);
        tokenRepository.save(resetToken);

        // 5. メール送信
        sendEmail(email, token);
    }

    private void sendEmail(String to, String token) {
        // 本来は本番環境のURLを指定する
        String resetUrl = "http://localhost:8080/springlesson/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("【重要】パスワード再設定のご案内");
        message.setText("いつもご利用ありがとうございます。\n\n"
                + "以下のリンクをクリックして、30分以内にパスワードの再設定を完了させてください。\n"
                + resetUrl + "\n\n"
                + "※このメールに心当たりがない場合は、破棄してください。");
        
        mailSender.send(message);
    }
 // --- 以下を追記 ---

    /**
     * 1. トークンが有効かどうかをチェックする
     */
    public boolean validatePasswordResetToken(String token) {
        return tokenRepository.findByToken(token)
                .map(resetToken -> !resetToken.isExpired()) // 期限切れでなければtrue
                .orElse(false); // トークンが存在しなければfalse
    }

    /**
     * 2. 新しいパスワードをハッシュ化して保存する
     */
    @Transactional
    public void updatePassword(String token, String newPassword) throws Exception {
        // トークンからユーザーを特定
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new Exception("無効なトークンです"));

        if (resetToken.isExpired()) {
            throw new Exception("トークンの有効期限が切れています");
        }

        User user = resetToken.getUser();
        
        // パスワードをハッシュ化して更新
        // ※ passwordEncoderの注入が必要です（後述）
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // セキュリティのため、一度使ったトークンは削除する
        tokenRepository.delete(resetToken);
    }
}