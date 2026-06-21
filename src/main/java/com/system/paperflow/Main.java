package com.system.paperflow;

import com.system.paperflow.application.gateway.EmailGateway;
import com.system.paperflow.application.usecase.SendEmailUseCase;
import com.system.paperflow.domain.email.AcceptedEmailTemplate;
import com.system.paperflow.domain.email.EmailMessage;
import com.system.paperflow.domain.email.EmailTemplate;
import com.system.paperflow.infrastructure.gateway.JakartaMailGateway;

public class Main {

    public static void main(String[] args) {
        
        int port = 000;
        String host = "...";
        String username = "...";
        String password = "...";

        String recepient = "...";
        
        System.out.println("Iniciando envio do email...");
        EmailGateway gateway = JakartaMailGateway.builder().withHost(host).withPassword(password).withPort(port).withUsername(username).build();

        SendEmailUseCase useCase = new SendEmailUseCase(gateway);

        EmailTemplate template = new AcceptedEmailTemplate();
        EmailMessage message = EmailMessage.builder().withRecipient(recepient).withSubject("Teste de Email").withTemplate(template).build();

        useCase.execute(message);

        System.out.println("Messagem enviada!");
        
    }
    
}