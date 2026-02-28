package com.example.ebike_testing_system.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTestReport(String toEmail, String reportUrl) throws MessagingException {
        MimeMessage  message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your Test Report");
        helper.setText("Dear Customer,\n\nYour test report is available at the following link:\n" + reportUrl);

        mailSender.send(message);
    }

    public void sendQRCodeEmail(String toEmail, String bikeId, String qrCodeBase64) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Your Bike Test Report QR Code");
            helper.setText("Dear Customer,\n\nScan the attached QR code to view your bike's test history.");

            // Decode Base64 and attach the QR code image
            byte[] imageBytes = Base64.getDecoder().decode(qrCodeBase64);
            File tempFile = File.createTempFile("QRCode_" + bikeId, ".png");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(imageBytes);
            }
            helper.addAttachment("QRCode.png", tempFile);

            mailSender.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Failed to send email with QR code", e);
        }
    }
}