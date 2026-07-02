package com.system.paperflow.application.usecase.notification;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.EmailGateway;
import com.system.paperflow.application.gateway.PaperGateway;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.domain.entity.EmailMessage;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.domain.enums.PaperStatus;

import java.util.List;

public class NotifyAuthorsUseCase {

    private final EventManager eventManager;
    private final PaperGateway paperGateway;
    private final ReviewAssignmentGateway assignmentGateway;
    private final EmailGateway emailGateway;

    public NotifyAuthorsUseCase(
            EventManager eventManager,
            PaperGateway paperGateway,
            ReviewAssignmentGateway assignmentGateway,
            EmailGateway emailGateway
    ) {
        this.eventManager = eventManager;
        this.paperGateway = paperGateway;
        this.assignmentGateway = assignmentGateway;
        this.emailGateway = emailGateway;
    }

    public int execute() {
        if (!eventManager.hasEvent()) {
            throw new IllegalStateException("Nenhum evento ativo.");
        }

        Event event = eventManager.getCurrentEvent();
        int sent = 0;

        for (Paper paper : paperGateway.findByEvent(event)) {
            if (paper.getStatus() == PaperStatus.ACCEPTED || paper.getStatus() == PaperStatus.REJECTED) {
                String body = buildBody(event, paper, assignmentGateway.findByPaperId(paper.getId()));

                EmailMessage message = EmailMessage.builder()
                        .withRecipient(paper.getAuthor().getEmail())
                        .withSubject("Resultado da submissão - " + event.getName())
                        .withBody(body)
                        .withTemplate(() -> body)
                        .build();

                emailGateway.sendMail(message);
                sent++;
            }
        }

        return sent;
    }

    private String buildBody(Event event, Paper paper, List<ReviewAssignment> assignments) {
        StringBuilder builder = new StringBuilder();
        boolean accepted = paper.getStatus() == PaperStatus.ACCEPTED;

        builder.append("Prezado(a) ").append(paper.getAuthor().getUsername()).append(",\n\n");

        if (accepted) {
            builder.append("Parabéns! Sua submissão de nº ").append(paper.getId())
                    .append(", intitulada \"").append(paper.getTitle()).append("\", para ")
                    .append(event.getName()).append(" - ").append(event.getCategory())
                    .append(", foi aceita.\n\n");
        } else {
            builder.append("Lamentamos informar que seu artigo de nº ").append(paper.getId())
                    .append(", intitulado \"").append(paper.getTitle()).append("\", não pôde ser aceito para ")
                    .append(event.getName()).append(" - ").append(event.getCategory())
                    .append(".\n\n");
        }

        builder.append("As avaliações seguem abaixo.\n\n");

        int index = 1;
        for (ReviewAssignment assignment : assignments) {
            if (assignment.isFinished()) {
                builder.append("[Revisor ").append(index++).append("]\n");
                builder.append("Principal contribuição ou pontos positivos\n");
                builder.append(assignment.getReview().getContribution()).append("\n\n");
                builder.append("Pontos negativos\n");
                builder.append(assignment.getReview().getCriticism()).append("\n\n");
                builder.append("Veredito: ").append(assignment.getReview().getVerdict()).append("\n\n");
            }
        }

        builder.append("Atenciosamente,\nCoordenação do Comitê de Programa");
        return builder.toString();
    }
}
