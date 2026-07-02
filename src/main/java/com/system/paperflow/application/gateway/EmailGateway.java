package com.system.paperflow.application.gateway;

import com.system.paperflow.domain.entity.EmailMessage;

import java.util.List;

public interface EmailGateway {
    
    void sendMail(EmailMessage message);

    List<EmailMessage> sentMessages();

}
