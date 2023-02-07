package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.model.messageData.EmailDetails;
import com.uber.app.team23.AirRide.model.users.PasswordResetData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PasswordResetDataService passwordResetDataService;
    @Value("${spring.mail.username}") private String sender;

    public void sendActivationMail(EmailDetails details, Long activationId){
        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            String link = "http://172.20.10.2:4200/confirmation?code=" + activationId.toString();
            helper.setText("<a href='"+ link + "'>Click to confirm</a>", true);
            helper.setFrom(sender);

            javaMailSender.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendResetPwCode(EmailDetails ed, String code, Long id) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(ed.getRecipient());
            helper.setSubject(ed.getSubject());
            helper.setText("Your Password Reset Code is: " + code, false);
            helper.setFrom(sender);

            passwordResetDataService.save(new PasswordResetData(id, code));

            javaMailSender.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
