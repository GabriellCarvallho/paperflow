package com.system.paperflow.application.observer;

import com.system.paperflow.application.gateway.EmailGateway;
import com.system.paperflow.application.observer.event.PaperReviewEvent;
import com.system.paperflow.domain.entity.Paper;

public class AuthorNotificationObserver implements PaperReviewObserver {

    private final EmailGateway emailGateway;

    public AuthorNotificationObserver(EmailGateway emailGateway) {
        this.emailGateway = emailGateway;
    }

    @Override
    public void update(PaperReviewEvent event) {


    }
}