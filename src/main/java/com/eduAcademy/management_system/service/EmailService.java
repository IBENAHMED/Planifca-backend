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

        // Contenu HTML de l'email avec le lien d'activation
        String emailContent = "<html>" +
                "<body>" +
                "<p>Bonjour,</p>" +
                "<p>Merci de créer un compte chez nous. Veuillez activer votre compte en cliquant sur le lien ci-dessous :</p>" +
                "<a href='" + link + "'>Activer votre compte</a>" +
                "<p>Si vous n'avez pas demandé cette inscription, ignorez cet email.</p>" +
                "</body>" +
                "</html>";

        // Configurer le MimeMessage
        mimeMessageHelper.setText(emailContent, true); // `true` pour indiquer que c'est du HTML
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject("Activation de votre compte"); // Sujet de l'email
        mimeMessageHelper.setFrom("no-reply@gmail.com");

        // Envoyer l'email
        mailSender.send(mimeMessage);
    }}
