package com.system.paperflow.infrastructure.memory;

import com.system.paperflow.application.gateway.EmailGateway;
import com.system.paperflow.domain.entity.EmailMessage;

import java.util.ArrayList;
import java.util.List;

public class InMemoryEmailGateway implements EmailGateway {

    private final List<EmailMessage> sentMessages = new ArrayList<>();

    @Override
    public void sendMail(EmailMessage message) {
        sentMessages.add(message);
    }

    public List<EmailMessage> sentMessages() {
        return List.copyOf(sentMessages);
    }
}
