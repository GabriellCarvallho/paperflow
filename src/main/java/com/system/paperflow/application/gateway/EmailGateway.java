package com.system.paperflow.application.gateway;

import com.system.paperflow.domain.entity.EmailMessage;

public interface EmailGateway {
    
    void sendMail(EmailMessage message);

}
