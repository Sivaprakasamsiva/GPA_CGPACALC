package com.AU.GPA_CGPACALC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String name, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("GPA Calculator - Email Verification");
        message.setText("Dear " + name + ",\n\n" +
                "Your OTP for email verification is: " + otp + "\n\n" +
                "This OTP will expire in 10 minutes.\n\n" +
                "Best regards,\nGPA Calculator Team");

        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String name, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("GPA Calculator - Password Reset");
        message.setText("Dear " + name + ",\n\n" +
                "You have requested to reset your password. Please use the following token:\n\n" +
                resetToken + "\n\n" +
                "This token will expire in 1 hour.\n\n" +
                "Best regards,\nGPA Calculator Team");

        mailSender.send(message);
    }
}