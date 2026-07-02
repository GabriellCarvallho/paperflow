package com.system.paperflow.application.factory;

import com.system.paperflow.application.dto.AuthorNotificationData;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.PaperStatus;
import com.system.paperflow.domain.template.AcceptedEmailTemplate;
import com.system.paperflow.domain.template.EmailTemplate;
import com.system.paperflow.domain.template.RejectedEmailTemplate;

public class EmailTemplateFactory {

    public EmailTemplate create(AuthorNotificationData data) {
        Paper paper = data.paper();

        if (paper.getStatus() == PaperStatus.ACCEPTED) {
            return new AcceptedEmailTemplate(paper, data.assignments());
        }

        if (paper.getStatus() == PaperStatus.REJECTED) {
            return new RejectedEmailTemplate(paper, data.assignments());
        }

        throw new IllegalArgumentException("Nao existe template para o status " + paper.getStatus() + ".");
    }
}
