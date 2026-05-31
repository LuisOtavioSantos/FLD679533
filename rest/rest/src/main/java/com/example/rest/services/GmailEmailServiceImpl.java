package com.example.rest.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@Profile("prod") // Gmail somente em produção
@RequiredArgsConstructor
public class GmailEmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(GmailEmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.backend-url:https://api.seudominio.com}")
    private String backendUrl;

    @Async
    @Override
    public void sendVerificationEmail(String toEmail, String firstName, String token) {
        logger.info("[Gmail Production] Iniciando envio real de e-mail para: {}", toEmail);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "DevShop Oficial");
            helper.setTo(toEmail);
            helper.setSubject("DevShop - Ative sua conta agora!");

            String verificationLink = backendUrl + "/auth/verify?token=" + token;

            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<h2 style='color: #0f172a;'>Bem-vindo à DevShop, " + firstName + "!</h2>"
                    + "<p>Falta apenas um passo para finalizar seu cadastro e aproveitar nossas ofertas.</p>"
                    + "<br>"
                    + "<a href='" + verificationLink
                    + "' style='background-color: #3b82f6; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px; font-weight: bold;'>VERIFICAR MEU E-MAIL</a>"
                    + "<br><br>"
                    + "<p>Ou copie e cole o link no seu navegador:</p>"
                    + "<p><a href='" + verificationLink + "'>" + verificationLink + "</a></p>"
                    + "<hr style='border: none; border-top: 1px solid #e2e8f0; margin-top: 30px;'>"
                    + "<p style='font-size: 12px; color: #64748b;'>Se você não solicitou este cadastro, pode ignorar este e-mail.</p>"
                    + "</div>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("[Gmail Production] E-mail real enviado com sucesso para: {}", toEmail);

        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            logger.error("[Gmail Production] Falha ao enviar e-mail real para {}: {}", toEmail, e.getMessage());
        }
    }
}
