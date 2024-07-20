package com.tas.applicazionebancaria.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromMail;
    /**
     * Specificare in ordine: email mittente, oggetto e corpo
     */
    public void sendEmail(String to, String oggetto, String body) {
    	System.out.println("sto inviando un email");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(to);
        message.setSubject(oggetto);
        message.setText(body);

        mailSender.send(message);
    }
}