package com.system.paperflow.infrastructure.gateway;

import java.util.Properties;

import com.system.paperflow.application.gateway.EmailGateway;
import com.system.paperflow.domain.entity.EmailMessage;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class JakartaMailGateway implements EmailGateway {

    private final Session session;
    private final String from;

    public JakartaMailGateway(Session session, String from) {
        this.session = session;
        this.from = from;
    }

    @Override
    public void sendMail(EmailMessage message) {
        try {
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(new InternetAddress(from));

            mimeMessage.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(message.getTo())
            );

            mimeMessage.setSubject(
                    message.getSubject(),
                    "UTF-8"
            );

            mimeMessage.setContent(
                    message.getTemplate().template(),
                    "text/html; charset=UTF-8"
            );

            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }

    public static JakartaMailGatewayBuilder builder() {
        return new JakartaMailGatewayBuilder();
    }
    
    public static class JakartaMailGatewayBuilder {

        private int port;
        private String host, username, password;

        public JakartaMailGatewayBuilder withPort(int port) {
            this.port = port;
            return this;
        }
        
        public JakartaMailGatewayBuilder withHost(String host) {
            this.host = host;
            return this;
        }

        public JakartaMailGatewayBuilder withUsername(String username) {
            this.username = username;
            return this;
        }
        
        public JakartaMailGatewayBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public JakartaMailGateway build() {

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", String.valueOf(port));

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            return new JakartaMailGateway(session, username);
        }

    }
}
