package com.system.paperflow.application.observer;

import com.system.paperflow.application.dto.AuthorNotificationData;
import com.system.paperflow.application.factory.EmailTemplateFactory;
import com.system.paperflow.application.gateway.EmailGateway;
import com.system.paperflow.domain.entity.EmailMessage;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.PaperStatus;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.domain.template.EmailTemplate;

import java.util.List;

public class AuthorNotificationObserver implements PaperReviewObserver {

    private final EmailGateway emailGateway;
    private final EmailTemplateFactory emailTemplateFactory;

    public AuthorNotificationObserver(EmailGateway emailGateway, EmailTemplateFactory emailTemplateFactory) {
        this.emailGateway = emailGateway;
        this.emailTemplateFactory = emailTemplateFactory;
    }

    @Override
    public void update(Paper paper, List<ReviewAssignment> assignments) {

        EmailTemplate template = emailTemplateFactory.create(new AuthorNotificationData(paper, assignments));

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
