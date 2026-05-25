package com.foodordering.service;

import com.foodordering.entity.Order;
import com.foodordering.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Async
    public void sendRegistrationEmail(User user) {
        String subject = "Welcome to Food Ordering!";
        String body = String.format(
                "Hello %s,\n\nYour registration was successful. You can now browse restaurants and place orders.\n\nThank you!",
                user.getName());
        sendEmail(user.getEmail(), subject, body);
    }

    @Async
    public void sendOrderConfirmationEmail(User user, Order order) {
        String subject = "Order Confirmation #" + order.getId();
        String body = String.format(
                "Hello %s,\n\nYour order #%d has been placed successfully.\nTotal: $%s\nStatus: %s\n\nThank you for ordering!",
                user.getName(), order.getId(), order.getTotalAmount(), order.getStatus());
        sendEmail(user.getEmail(), subject, body);
    }

    @Async
    public void sendOrderCancellationEmail(User user, Order order) {
        String subject = "Order Cancelled #" + order.getId();
        String body = String.format(
                "Hello %s,\n\nYour order #%d has been cancelled.\nTotal refunded amount: $%s\n\nWe hope to serve you again soon.",
                user.getName(), order.getId(), order.getTotalAmount());
        sendEmail(user.getEmail(), subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent to {} - {}", to, subject);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
