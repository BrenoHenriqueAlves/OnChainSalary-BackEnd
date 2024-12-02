package com.example.OnChainSalary.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendVerificationEmail(String toEmail, String verificationCode, String userName) {
        try {
            // Criação da mensagem MIME
            MimeMessage message = javaMailSender.createMimeMessage();

            // Usando MimeMessageHelper para configurar o e-mail com HTML
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            // Definir destinatário, assunto e conteúdo HTML
            helper.setTo(toEmail);
            helper.setSubject("Código de Verificação - OnChainSalary");

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<html><body>");
            emailContent.append("<h2>🚀 <strong>Bem-vindo à OnChainSalary, ").append(userName).append("!</strong> 🎉</h2>");
            emailContent.append("<p>Ficamos muito felizes em ter você conosco! Juntos, vamos inovar a forma como os pagamentos são feitos e transformar o futuro do trabalho.</p>");
            emailContent.append("<p><strong>Seu código de verificação é: ").append(verificationCode).append("</strong></p>");
            emailContent.append("<p>Este código é válido por 15 minutos, então aproveite para confirmar seu e-mail o quanto antes!</p>");
            emailContent.append("<p>Se você não solicitou esse código, pode ignorar este e-mail. Não se preocupe, sua conta está segura.</p>");
            emailContent.append("<br/><p>Atenciosamente,</p>");
            emailContent.append("<p><strong>Equipe OnChainSalary</strong></p>");
            emailContent.append("</body></html>");

            // Enviar o e-mail com o conteúdo HTML
            helper.setText(emailContent.toString(), true);

            // Envio do e-mail
            javaMailSender.send(message);
        } catch (MessagingException e) {
            // Aqui você pode fazer o log do erro ou lançar uma exceção customizada
            e.printStackTrace(); // Exemplo de log de erro
            // Pode adicionar lógica de fallback ou notificação para o desenvolvedor
        }
    }

}
