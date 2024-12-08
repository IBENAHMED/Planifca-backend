package com.eduAcademy.management_system.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String link) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

        String emailContent = "<html>" +
                "<body>" +
                "<p>Bonjour,</p>" +
                "<p>Merci de créer un compte chez nous. Veuillez activer votre compte en cliquant sur le lien ci-dessous :</p>" +
                "<a href='" + link + "'>Activer votre compte</a>" +
                "<p>Si vous n'avez pas demandé cette inscription, ignorez cet email.</p>" +
                "</body>" +
                "</html>";

        mimeMessageHelper.setText(emailContent, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject("Activation de votre compte");
        mimeMessageHelper.setFrom("no-reply@gmail.com");
        mailSender.send(mimeMessage);
    }}
