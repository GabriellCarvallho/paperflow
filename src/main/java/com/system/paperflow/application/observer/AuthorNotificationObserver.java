package com.system.paperflow.application.observer;

import com.system.paperflow.application.gateway.EmailGateway;
import com.system.paperflow.domain.entity.EmailMessage;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.PaperStatus;
import com.system.paperflow.domain.template.AcceptedEmailTemplate;
import com.system.paperflow.domain.template.EmailTemplate;
import com.system.paperflow.domain.template.RejectedEmailTemplate;

public class AuthorNotificationObserver implements PaperReviewObserver {

    private final EmailGateway emailGateway;

    public AuthorNotificationObserver(EmailGateway emailGateway) {
        this.emailGateway = emailGateway;
    }

    @Override
    public void update(Paper paper) {

        EmailTemplate template = paper.getStatus() == PaperStatus.ACCEPTED
                ? new AcceptedEmailTemplate(paper) : new RejectedEmailTemplate(paper);

        String subject = paper.getStatus() == PaperStatus.ACCEPTED ? "Artigo aceito - " + paper.getTitle()
                : "Resultado da submissão - " + paper.getTitle();

        EmailMessage message = EmailMessage.builder()
                .withRecipient(paper.getAuthor().getEmail())
                .withSubject(subject)
                .withTemplate(template)
                .build();

        emailGateway.sendMail(message);
    }
}