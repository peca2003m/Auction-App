package com.auction.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    public void sendRegistrationEmail(String to, UUID token){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Complete your Registration");
        message.setText("Click the link to complete registration: http://localhost:5173/register?token=" +token);
        mailSender.send(message);

    }


}
