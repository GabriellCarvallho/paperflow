package com.system.paperflow.application.usecase.email;

import com.system.paperflow.application.gateway.EmailGateway;
import com.system.paperflow.domain.entity.EmailMessage;

public class SendEmailUseCase {
    
    private final EmailGateway gateway;

    public SendEmailUseCase(EmailGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(EmailMessage message) {
        gateway.sendMail(message);
    }

}
