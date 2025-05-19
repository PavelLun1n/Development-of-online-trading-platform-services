package com.example.service;

import com.example.form.CheckoutForm;
import com.example.model.Order;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public byte[] generateQrCode(String data, int width, int height) {
        try {
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(data, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации QR-кода", e);
        }
    }

    public void sendOrderConfirmation(
            String to,
            CheckoutForm form,
            List<Order> orders,
            byte[] qrImage
    ) {
        MimeMessage msg = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Ваш заказ на Marketplace");

            StringBuilder sb = new StringBuilder();
            sb.append("Здравствуйте, ").append(form.getFullName()).append("!\n\n");
            sb.append("Ваш заказ принят:\n");
            for (Order o : orders) {
                sb.append("- Заказ №").append(o.getId())
                        .append(" (").append(o.getProduct().getName()).append(") ")
                        .append("x").append(o.getQuantity())
                        .append(" — ").append(o.getTotalPrice()).append(" ₽\n");
            }
            sb.append("\nАдрес доставки: ").append(form.getAddress()).append("\n");
            sb.append("Телефон: ").append(form.getPhone()).append("\n");
            sb.append("Спасибо за покупку!\n");

            helper.setText(sb.toString());
            helper.addInline("qr", new ByteArrayResource(qrImage), "image/png");

            mailSender.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException("Не удалось отправить письмо с подтверждением", e);
        }
    }
}
