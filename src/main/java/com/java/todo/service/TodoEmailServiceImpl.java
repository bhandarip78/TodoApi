package com.java.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("TodoEmailService")
public class TodoEmailServiceImpl implements TodoEmailService{

    @Autowired
    private JavaMailSender mailSender;

    public void sendMessage(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "utf-8");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);

            mailSender.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}