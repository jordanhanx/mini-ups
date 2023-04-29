package edu.duke.ece568.team24.miniups.service;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final ThreadPoolTaskExecutor executor;

    private final JavaMailSender javaMailSender;

    private final String outForDeliverySubject;

    private final String outForDeliveryBody;

    private final String deliveredSubject;

    private final String deliveredBody;

    public EmailService(ThreadPoolTaskExecutor executor, JavaMailSender javaMailSender,
            @Value("${email.outForDelivery.subject}") String outForDeliverySubject,
            @Value("${email.outForDelivery.body}") String outForDeliveryBody,
            @Value("${email.delivered.subject}") String deliveredSubject,
            @Value("${email.delivered.body}") String deliveredBody) {
        this.executor = executor;
        this.javaMailSender = javaMailSender;
        this.outForDeliverySubject = outForDeliverySubject;
        this.outForDeliveryBody = outForDeliveryBody;
        this.deliveredSubject = deliveredSubject;
        this.deliveredBody = deliveredBody;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendOutForDeliveryEmail(String to, String username, int orderNumber, long trackingNumber) {
        executor.execute(() -> {
            try {
                String subject = outForDeliverySubject;
                String body = outForDeliveryBody.replace("{username}", username)
                        .replace("{order_number}", String.valueOf(orderNumber))
                        .replace("{tracking_number}", String.valueOf(trackingNumber));
                sendSimpleEmail(to, subject, body);
                logger.debug("[UPS]sendOutForDeliveryEmail() to " + to);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
    }

    public void sendDeliveredEmail(String to, String username, int orderNumber, long trackingNumber,
            Date deliveryDate) {
        executor.execute(() -> {
            try {
                String subject = deliveredSubject;
                String body = deliveredBody.replace("{username}", username)
                        .replace("{order_number}", String.valueOf(orderNumber))
                        .replace("{tracking_number}", String.valueOf(trackingNumber))
                        .replace("{delivery_date}", deliveryDate.toString());
                sendSimpleEmail(to, subject, body);
                logger.debug("[UPS]sendDeliveredEmail() to " + to);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
    }
}
