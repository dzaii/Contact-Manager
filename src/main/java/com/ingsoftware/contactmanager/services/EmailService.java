package com.ingsoftware.contactmanager.services;

import lombok.AllArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender emailSender;

    @Async
    public void sendConfirmationEmail(String recipient) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("testing.things.email.for@gmail.com");
            message.setTo(recipient);
            message.setSubject("Welcome");
            message.setText("Welcome to CM.");

            emailSender.send(message);
   }
}
