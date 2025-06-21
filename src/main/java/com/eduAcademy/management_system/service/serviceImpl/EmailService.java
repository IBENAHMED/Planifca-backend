package com.eduAcademy.management_system.service.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private String loadEmailTemplate(String templateName) throws IOException {

        return new String(Files.readAllBytes(Paths.get("src/main/resources/EmailTemplates/"+templateName)));
    }

    public void sendPasswordResetEmail(String toEmail, String resetUrl) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String emailContent = loadEmailTemplate("reset-password.html");

        helper.setFrom("no-reply@planifca.com");
        helper.setTo(toEmail);
        helper.setSubject("Réinitialisation de votre mot de passe");
        helper.setText(emailContent.replace("{{resetUrl}}", resetUrl), true);

        mailSender.send(message);
    }

    public void sendEmailActivationAccount(String toEmail,String firstName,String lastName,String userId,String frontPath) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String emailContent = loadEmailTemplate("activationAccount.html")
                .replace("{{firstName}}", firstName)
                .replace("{{lastName}}", lastName)
                .replace("{{activationUrl}}","http://localhost:4200/activate-account?club="+frontPath+"?userId="+userId);


        helper.setFrom("no-reply@planifca.com");
        helper.setTo(toEmail);
        helper.setSubject("Activation de votre compte");
        helper.setText(emailContent, true);

        mailSender.send(message);
    }


}
