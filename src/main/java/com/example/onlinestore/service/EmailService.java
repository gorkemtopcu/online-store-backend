package com.example.onlinestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithAttachment(String toEmail, String subject, String body, String attachmentPath) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);

            // Dosya eklemek için
            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            helper.addAttachment("Invoice.pdf", file);

            mailSender.send(message);
            System.out.println("[INFO] Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            System.err.println("[ERROR] Error while sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}