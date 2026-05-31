package com.example.rest.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class MailpitEmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(MailpitEmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:devshop@mailpit.local}")
    private String fromEmail;

    @Value("${app.backend-url:http://localhost:8090}")
    private String backendUrl;

    @Async
    @Override
    public void sendVerificationEmail(String toEmail, String firstName, String token) {
        logger.info("[Mailpit Sandbox] Preparando e-mail de ativação de conta para: {}", toEmail);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "DevShop (Mailpit Dev)");
            helper.setTo(toEmail);
            helper.setSubject("DevShop - Ative sua conta de estudante!");

            String verificationLink = backendUrl + "/auth/verify?token=" + token;

            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<h2 style='color: #0f172a;'>[DevMode] Bem-vindo à DevShop, " + firstName + "!</h2>"
                    + "<p>Você está no ambiente acadêmico de testes. Valide sua conta clicando no botão abaixo:</p>"
                    + "<br>"
                    + "<a href='" + verificationLink
                    + "' style='background-color: #10b981; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px; font-weight: bold;'>VERIFICAR E-MAIL NO MAILPIT</a>"
                    + "<br><br>"
                    + "<p>Link de Ativação:</p>"
                    + "<p><a href='" + verificationLink + "'>" + verificationLink + "</a></p>"
                    + "<hr style='border: none; border-top: 1px solid #e2e8f0; margin-top: 30px;'>"
                    + "<p style='font-size: 12px; color: #64748b;'>Simulação SMTP via Mailpit.</p>"
                    + "</div>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("[Mailpit Sandbox] E-mail simulado enviado com sucesso para: {}", toEmail);

        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            logger.error("[Mailpit Sandbox] Falha ao enviar e-mail simulado para {}: {}", toEmail, e.getMessage());
        }
    }
}
