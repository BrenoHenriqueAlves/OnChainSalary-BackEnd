package com.example.OnChainSalary.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private JavaMailSender getJavaMailSender() {
        // Criação do JavaMailSender a partir das variáveis de ambiente
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Pegando as credenciais do ambiente
        mailSender.setHost(System.getenv("SPRING_MAIL_HOST"));
        mailSender.setPort(Integer.parseInt(System.getenv("SPRING_MAIL_PORT")));
        mailSender.setUsername(System.getenv("SPRING_MAIL_USERNAME"));
        mailSender.setPassword(System.getenv("SPRING_MAIL_PASSWORD"));

        // Configuração adicional de propriedades
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return mailSender;
    }

    public void sendVerificationEmail(String toEmail, String verificationCode, String userName) {
        try {
            // Criação do JavaMailSender com as credenciais do ambiente
            JavaMailSender mailSender = getJavaMailSender();

            // Criação da mensagem MIME
            MimeMessage message = mailSender.createMimeMessage();

            // Usando MimeMessageHelper para configurar o e-mail com HTML
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            // Definir destinatário, assunto e conteúdo HTML
            helper.setTo(toEmail);
            helper.setSubject("Código de Verificação - OnChainSalary");

            StringBuilder emailContent = new StringBuilder();
            String primeiroNome = userName.split(" ")[0];
            emailContent.append("<html><body>");

            emailContent.append("<h2>🚀 <strong>Bem-vindo à OnChainSalary, ").append(primeiroNome).append("!</strong> 🎉</h2>");
            emailContent.append("<p><strong>Seu código de verificação é: ").append(verificationCode).append("</strong></p>");
            emailContent.append("<p>Ficamos muito felizes em ter você conosco! Juntos, vamos inovar a forma como os pagamentos são feitos e transformar o futuro do trabalho.</p>");
            emailContent.append("<p>Este código é válido por 15 minutos, então aproveite para confirmar seu e-mail o quanto antes!</p>");
            emailContent.append("<p>Se você não solicitou esse código, pode ignorar este e-mail. Não se preocupe, sua conta está segura.</p>");
            emailContent.append("<br/><p>Atenciosamente,</p>");
            emailContent.append("<p><strong>Equipe OnChainSalary</strong></p>");
            emailContent.append("</body></html>");

            // Enviar o e-mail com o conteúdo HTML
            helper.setText(emailContent.toString(), true);

            // Envio do e-mail
            mailSender.send(message);
        } catch (MessagingException e) {
            // Aqui você pode fazer o log do erro ou lançar uma exceção customizada
            e.printStackTrace(); // Exemplo de log de erro
            // Pode adicionar lógica de fallback ou notificação para o desenvolvedor
        }
    }

}
