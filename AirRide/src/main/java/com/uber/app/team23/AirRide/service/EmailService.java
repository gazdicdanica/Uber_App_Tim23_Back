package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.model.messageData.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private String sender;

    public void sendSimpleMail(EmailDetails details){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(details.getRecipient());
        mailMessage.setText(details.getMessageBody());
        mailMessage.setSubject(details.getSubject());

        javaMailSender.send(mailMessage);
    }

    public String sendMailWithAttachment(EmailDetails details){
        return "";
    }
}
