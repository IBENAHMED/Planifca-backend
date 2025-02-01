package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.AuthenticationRequestDto;
import com.eduAcademy.management_system.dto.AuthenticationResponseDto;
import com.eduAcademy.management_system.dto.RegisterRequestDto;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender;

    public void register(RegisterRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("The email is already in use");
        }
        var user= User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .role(request.getRole())
                .build();
        userRepository.save(user);
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + request.getEmail()));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String jwtToken = jwtService.generateTokenForUser(user);

            return AuthenticationResponseDto.builder()
                    .token(jwtToken)
                    .build();
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect credentials", e);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred", e);
        }
    }

    public void sendPasswordResetEmail(String email) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) return;

        User user = userOptional.get();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiration(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        String resetUrl = "http://localhost:4200/reset-password?token=" + token;

//        String message = "<p>Cliquez sur le lien ci-dessous pour réinitialiser votre mot de passe :</p>" +
//                "<a href='" + resetUrl + "'>" + resetUrl + "</a><br>    " +
//                "<p>Ce lien est valide pendant 15 minutes.</p>";

        String message ="<style type=\"text/css\">\n" +
                "table {\n" +
                "border-collapse: separate;\n" +
                "table-layout: fixed;\n" +
                "mso-table-lspace: 0pt;\n" +
                "mso-table-rspace: 0pt\n" +
                "}\n" +
                "table td {\n" +
                "border-collapse: collapse\n" +
                "}\n" +
                ".ExternalClass {\n" +
                "width: 100%\n" +
                "}\n" +
                ".ExternalClass,\n" +
                ".ExternalClass p,\n" +
                ".ExternalClass span,\n" +
                ".ExternalClass font,\n" +
                ".ExternalClass td,\n" +
                ".ExternalClass div {\n" +
                "line-height: 100%\n" +
                "}\n" +
                "body, a, li, p, h1, h2, h3 {\n" +
                "-ms-text-size-adjust: 100%;\n" +
                "-webkit-text-size-adjust: 100%;\n" +
                "}\n" +
                "html {\n" +
                "-webkit-text-size-adjust: none !important\n" +
                "}\n" +
                "body, #innerTable {\n" +
                "-webkit-font-smoothing: antialiased;\n" +
                "-moz-osx-font-smoothing: grayscale\n" +
                "}\n" +
                "#innerTable img+div {\n" +
                "display: none;\n" +
                "display: none !important\n" +
                "}\n" +
                "img {\n" +
                "Margin: 0;\n" +
                "padding: 0;\n" +
                "-ms-interpolation-mode: bicubic\n" +
                "}\n" +
                "h1, h2, h3, p, a {\n" +
                "line-height: inherit;\n" +
                "overflow-wrap: normal;\n" +
                "white-space: normal;\n" +
                "word-break: break-word\n" +
                "}\n" +
                "a {\n" +
                "text-decoration: none\n" +
                "}\n" +
                "h1, h2, h3, p {\n" +
                "min-width: 100%!important;\n" +
                "width: 100%!important;\n" +
                "max-width: 100%!important;\n" +
                "display: inline-block!important;\n" +
                "border: 0;\n" +
                "padding: 0;\n" +
                "margin: 0\n" +
                "}\n" +
                "a[x-apple-data-detectors] {\n" +
                "color: inherit !important;\n" +
                "text-decoration: none !important;\n" +
                "font-size: inherit !important;\n" +
                "font-family: inherit !important;\n" +
                "font-weight: inherit !important;\n" +
                "line-height: inherit !important\n" +
                "}\n" +
                "u + #body a {\n" +
                "color: inherit;\n" +
                "text-decoration: none;\n" +
                "font-size: inherit;\n" +
                "font-family: inherit;\n" +
                "font-weight: inherit;\n" +
                "line-height: inherit;\n" +
                "}\n" +
                "a[href^=\"mailto\"],\n" +
                "a[href^=\"tel\"],\n" +
                "a[href^=\"sms\"] {\n" +
                "color: inherit;\n" +
                "text-decoration: none\n" +
                "}\n" +
                "</style>\n" +
                "<style type=\"text/css\">\n" +
                "@media (min-width: 481px) {\n" +
                ".hd { display: none!important }\n" +
                "}\n" +
                "</style>\n" +
                "<style type=\"text/css\">\n" +
                "@media (max-width: 480px) {\n" +
                ".hm { display: none!important }\n" +
                "}\n" +
                "</style>\n" +
                "<style type=\"text/css\">\n" +
                "@media (max-width: 480px) {\n" +
                ".t57{padding:0 0 22px!important}.t14,.t48,.t59,.t75{width:480px!important}.t42,.t53,.t69,.t8{text-align:center!important}.t41,.t52,.t68,.t7{vertical-align:top!important;width:600px!important}.t5{border-top-left-radius:0!important;border-top-right-radius:0!important;padding:20px 30px!important}.t39{border-bottom-right-radius:0!important;border-bottom-left-radius:0!important;padding:30px!important}.t77{mso-line-height-alt:20px!important;line-height:20px!important}.t64{width:380px!important}.t3{width:44px!important}.t25,.t37{width:420px!important}\n" +
                "}\n" +
                "</style>\n" +
                "<!--[if !mso]>-->\n" +
                "<link href=\"https://fonts.googleapis.com/css2?family=Albert+Sans:wght@500;800&amp;display=swap\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                "<!--<![endif]-->\n" +
                "<!--[if mso]>\n" +
                "<xml>\n" +
                "<o:OfficeDocumentSettings>\n" +
                "<o:AllowPNG/>\n" +
                "<o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "</o:OfficeDocumentSettings>\n" +
                "</xml>\n" +
                "<![endif]-->\n" +
                "</head>\n" +
                "<body id=\"body\" class=\"t80\" style=\"min-width:100%;Margin:0px;padding:0px;background-color:#E0E0E0;\"><div class=\"t79\" style=\"background-color:#E0E0E0;\"><table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tr><td class=\"t78\" style=\"font-size:0;line-height:0;mso-line-height-rule:exactly;background-color:#E0E0E0;\" valign=\"top\" align=\"center\">\n" +
                "<!--[if mso]>\n" +
                "<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"true\" stroke=\"false\">\n" +
                "<v:fill color=\"#E0E0E0\"/>\n" +
                "</v:background>\n" +
                "<![endif]-->\n" +
                "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" id=\"innerTable\"><tr><td align=\"center\">\n" +
                "<table class=\"t60\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"Margin-left:auto;Margin-right:auto;\"><tr>\n" +
                "<!--[if mso]>\n" +
                "<td width=\"566\" class=\"t59\" style=\"width:566px;\">\n" +
                "<![endif]-->\n" +
                "<!--[if !mso]>-->\n" +
                "<td class=\"t59\" style=\"width:566px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table class=\"t58\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"width:100%;\"><tr><td class=\"t57\" style=\"padding:50px 10px 31px 10px;\"><div class=\"t56\" style=\"width:100%;text-align:center;\"><div class=\"t55\" style=\"display:inline-block;\"><table class=\"t54\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" valign=\"top\">\n" +
                "<tr class=\"t53\"><td></td><td class=\"t52\" width=\"546\" valign=\"top\">\n" +
                "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" class=\"t51\" style=\"width:100%;\"><tr><td class=\"t50\" style=\"background-color:transparent;\"><table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100% !important;\"><tr><td align=\"center\">\n" +
                "<table class=\"t15\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"Margin-left:auto;Margin-right:auto;\"><tr>\n" +
                "<!--[if mso]>\n" +
                "<td width=\"546\" class=\"t14\" style=\"width:546px;\">\n" +
                "<![endif]-->\n" +
                "<!--[if !mso]>-->\n" +
                "<td class=\"t14\" style=\"width:546px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table class=\"t13\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"width:100%;\"><tr><td class=\"t12\"><div class=\"t11\" style=\"width:100%;text-align:center;\"><div class=\"t10\" style=\"display:inline-block;\"><table class=\"t9\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" valign=\"top\">\n" +
                "<tr class=\"t8\"><td></td><td class=\"t7\" width=\"546\" valign=\"top\">\n" +
                "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" class=\"t6\" style=\"width:100%;\"><tr><td class=\"t5\" style=\"overflow:hidden;background-color:#586CE0;padding:49px 50px 42px 50px;border-radius:18px 18px 0 0;\"><table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100% !important;\"><tr><td align=\"left\">\n" +
                "<table class=\"t4\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"Margin-right:auto;\"><tr>\n" +
                "<!--[if mso]>\n" +
                "<td width=\"85\" class=\"t3\" style=\"width:85px;\">\n" +
                "<![endif]-->\n" +
                "<!--[if !mso]>-->\n" +
                "<td class=\"t3\" style=\"width:85px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table class=\"t2\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"width:100%;\"><tr><td class=\"t1\"><div style=\"font-size:0px;\"><img class=\"t0\" style=\"display:block;border:0;height:auto;width:100%;Margin:0;max-width:100%;\" width=\"85\" height=\"85\" alt=\"\" src=\"https://0a73396a-30f7-462a-9332-df4506e3070f.b-cdn.net/e/b61b43fa-b625-49f0-b7b4-b3eca75fa972/2d30c70c-822c-4e5d-9825-ba74e7995021.png\"/></div></td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr></table></td></tr></table>\n" +
                "</td>\n" +
                "<td></td></tr>\n" +
                "</table></div></div></td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr><tr><td align=\"center\">\n" +
                "<table class=\"t49\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"Margin-left:auto;Margin-right:auto;\"><tr>\n" +
                "<!--[if mso]>\n" +
                "<td width=\"546\" class=\"t48\" style=\"width:546px;\">\n" +
                "<![endif]-->\n" +
                "<!--[if !mso]>-->\n" +
                "<td class=\"t48\" style=\"width:546px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table class=\"t47\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"width:100%;\"><tr><td class=\"t46\"><div class=\"t45\" style=\"width:100%;text-align:center;\"><div class=\"t44\" style=\"display:inline-block;\"><table class=\"t43\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" valign=\"top\">\n" +
                "<tr class=\"t42\"><td></td><td class=\"t41\" width=\"546\" valign=\"top\">\n" +
                "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" class=\"t40\" style=\"width:100%;\"><tr><td class=\"t39\" style=\"overflow:hidden;background-color:#F8F8F8;padding:40px 50px 40px 50px;border-radius:0 0 18px 18px;\"><table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100% !important;\"><tr><td align=\"left\">\n" +
                "<table class=\"t20\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"Margin-right:auto;\"><tr>\n" +
                "<!--[if mso]>\n" +
                "<td width=\"381\" class=\"t19\" style=\"width:381px;\">\n" +
                "<![endif]-->\n" +
                "<!--[if !mso]>-->\n" +
                "<td class=\"t19\" style=\"width:381px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table class=\"t18\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"width:100%;\"><tr><td class=\"t17\"><h1 class=\"t16\" style=\"margin:0;Margin:0;font-family:Albert Sans,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:41px;font-weight:800;font-style:normal;font-size:30px;text-decoration:none;text-transform:none;letter-spacing:-1.56px;direction:ltr;color:#191919;text-align:left;mso-line-height-rule:exactly;mso-text-raise:3px;\">Mot de passe oublié ?<br/>Cela arrive aux meilleurs d'entre nous.</h1></td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr><tr><td><div class=\"t21\" style=\"mso-line-height-rule:exactly;mso-line-height-alt:25px;line-height:25px;font-size:1px;display:block;\">&nbsp;&nbsp;</div></td></tr><tr><td align=\"left\">\n" +
                "<table class=\"t26\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"Margin-right:auto;\"><tr>\n" +
                "<!--[if mso]>\n" +
                "<td width=\"446\" class=\"t25\" style=\"width:446px;\">\n" +
                "<![endif]-->\n" +
                "<!--[if !mso]>-->\n" +
                "<td class=\"t25\" style=\"width:446px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table class=\"t24\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"width:100%;\"><tr><td class=\"t23\"><p class=\"t22\" style=\"margin:0;Margin:0;font-family:Albert Sans,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:22px;font-weight:500;font-style:normal;font-size:14px;text-decoration:none;text-transform:none;letter-spacing:-0.56px;direction:ltr;color:#333333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px;\">Pour réinitialiser votre mot de passe, cliquez sur le bouton ci-dessous. Le lien s'autodétruira après cinq jours.</p></td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr><tr><td><div class=\"t27\" style=\"mso-line-height-rule:exactly;mso-line-height-alt:15px;line-height:15px;font-size:1px;display:block;\">&nbsp;&nbsp;</div></td></tr><tr><td align=\"left\">\n" +
                "<table class=\"t32\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"Margin-right:auto;\"><tr>\n" +
                "<!--[if mso]>\n" +
                "<td width=\"234\" class=\"t31\" style=\"background-color:#586CE0;overflow:hidden;width:234px;border-radius:40px 40px 40px 40px;\">\n" +
                "<![endif]-->\n" +
                "<!--[if !mso]>-->\n" +
                "<td class=\"t31\" style=\"background-color:#586CE0;overflow:hidden;width:234px;border-radius:40px 40px 40px 40px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table class=\"t30\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"width:100%;\"><tr><td class=\"t29\" style=\"text-align:center;line-height:44px;mso-line-height-rule:exactly;mso-text-raise:10px;padding:0 30px 0 30px;\"><a class=\"t28\" href='\" + resetUrl + \"' style=\"display:block;margin:0;Margin:0;font-family:Albert Sans,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:44px;font-weight:800;font-style:normal;font-size:12px;text-decoration:none;text-transform:uppercase;letter-spacing:2.4px;direction:ltr;color:#FFFFFF;text-align:center;mso-line-height-rule:exactly;mso-text-raise:10px;\" target=\"_blank\">Réinitialiser le mp</a></td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr><tr><td><div class=\"t33\" style=\"mso-line-height-rule:exactly;mso-line-height-alt:15px;line-height:15px;font-size:1px;display:block;\">&nbsp;&nbsp;</div></td></tr><tr><td align=\"left\">\n" +
                "<table class=\"t38\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"Margin-right:auto;\"><tr>\n" +
                "<!--[if mso]>\n" +
                "<td width=\"446\" class=\"t37\" style=\"width:446px;\">\n" +
                "<![endif]-->\n" +
                "<!--[if !mso]>-->\n" +
                "<td class=\"t37\" style=\"width:446px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table class=\"t36\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"width:100%;\"><tr><td class=\"t35\"><p class=\"t34\" style=\"margin:0;Margin:0;font-family:Albert Sans,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:22px;font-weight:500;font-style:normal;font-size:14px;text-decoration:none;text-transform:none;letter-spacing:-0.56px;direction:ltr;color:#333333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px;\">Si vous avez des questions ou avez besoin d'aide, n'hésitez pas à contacter notre équipe de support en répondant à cet email ou en visitant notre page de support.\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n</p></td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr></table></td></tr></table>\n" +
                "</td>\n" +
                "<td></td></tr>\n" +
                "</table></div></div></td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr></table></td></tr></table>\n" +
                "</td>\n" +
                "<td></td></tr>\n" +
                "</table></div></div></td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr><tr><td align=\"center\">\n" +
                "<table class=\"t76\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"Margin-left:auto;Margin-right:auto;\"><tr>\n" +
                "<!--[if mso]>\n" +
                "<td width=\"600\" class=\"t75\" style=\"width:600px;\">\n" +
                "<![endif]-->\n" +
                "<!--[if !mso]>-->\n" +
                "<td class=\"t75\" style=\"width:600px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table class=\"t74\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"width:100%;\"><tr><td class=\"t73\"><div class=\"t72\" style=\"width:100%;text-align:center;\"><div class=\"t71\" style=\"display:inline-block;\"><table class=\"t70\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" valign=\"top\">\n" +
                "<tr class=\"t69\"><td></td><td class=\"t68\" width=\"600\" valign=\"top\">\n" +
                "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" class=\"t67\" style=\"width:100%;\"><tr><td class=\"t66\" style=\"padding:0 50px 0 50px;\"><table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100% !important;\"><tr><td align=\"center\">\n" +
                "<table class=\"t65\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"Margin-left:auto;Margin-right:auto;\"><tr>\n" +
                "<!--[if mso]>\n" +
                "<td width=\"420\" class=\"t64\" style=\"width:420px;\">\n" +
                "<![endif]-->\n" +
                "<!--[if !mso]>-->\n" +
                "<td class=\"t64\" style=\"width:420px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table class=\"t63\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"width:100%;\"><tr><td class=\"t62\"><p class=\"t61\" style=\"margin:0;Margin:0;font-family:Albert Sans,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:22px;font-weight:500;font-style:normal;font-size:12px;text-decoration:none;text-transform:none;direction:ltr;color:#888888;text-align:center;mso-line-height-rule:exactly;mso-text-raise:3px;\">© 2022 Flash Inc. All Rights Reserved<br/></p></td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr></table></td></tr></table>\n" +
                "</td>\n" +
                "<td></td></tr>\n" +
                "</table></div></div></td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr><tr><td><div class=\"t77\" style=\"mso-line-height-rule:exactly;mso-line-height-alt:50px;line-height:50px;font-size:1px;display:block;\">&nbsp;&nbsp;</div></td></tr></table></td></tr></table></div><div class=\"gmail-fix\" style=\"display: none; white-space: nowrap; font: 15px courier; line-height: 0;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div></body>\n" +
                "</html>";


        mimeMessageHelper.setText(message,true);
        mimeMessageHelper.setTo(user.getEmail());
        mimeMessageHelper.setSubject("Réinitialisation de votre mot de passe");
        mimeMessageHelper.setFrom("no-reply@gmail.com");
        mailSender.send(mimeMessage);
    }


    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        if (user.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            return false;
        }

        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiration(null);
        userRepository.save(user);

        return true;
    }



}