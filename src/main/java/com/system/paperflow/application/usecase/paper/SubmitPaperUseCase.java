package com.system.paperflow.application.usecase.paper;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.PaperGateway;
import com.system.paperflow.application.gateway.UserGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.ThematicArea;

import java.util.List;
import java.util.UUID;

public class SubmitPaperUseCase {

    private final EventManager manager;
    private final PaperGateway paperGateway;
    private final UserGateway userGateway;

    public SubmitPaperUseCase(EventManager manager, PaperGateway paperGateway) {
        this(manager, paperGateway, null);
    }

    public SubmitPaperUseCase(EventManager manager, PaperGateway paperGateway, UserGateway userGateway) {
        this.manager = manager;
        this.paperGateway = paperGateway;
        this.userGateway = userGateway;
    }

    public Paper execute(Researcher user, UUID eventId, String title, String summary, List<String> thematicAreas) {
        return execute(user, eventId, title, summary, thematicAreas, List.of());
    }

    public Paper execute(Researcher user, UUID eventId, String title, String summary, List<String> thematicAreas, List<String> collaboratorEmails) {

        if (!manager.hasEvent()) {
            throw new IllegalStateException("Nenhum evento ativo.");
        }

        Event event = manager.getCurrentEvent();

        if (!event.getId().equals(eventId)) {
            throw new IllegalArgumentException("Evento não encontrado.");
        }

        if (!event.isOpenForSubmission()) {
            throw new IllegalStateException("O evento não está aberto para submissão.");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Informe o título do artigo.");
        }

        if (summary == null || summary.isBlank()) {
            throw new IllegalArgumentException("Informe o resumo do artigo.");
        }

        if (thematicAreas == null || thematicAreas.isEmpty()) {
            throw new IllegalArgumentException("Escolha pelo menos uma área temática.");
        }

        Paper paper = new Paper(title.trim(), summary.trim(), user, event);

        for (String area : thematicAreas) {
            if (area != null && !area.isBlank()) {
                paper.addThematicArea(new ThematicArea(area.trim()));
            }
        }

        if (collaboratorEmails != null && !collaboratorEmails.isEmpty()) {
            if (userGateway == null) {
                throw new IllegalStateException("Não foi possível validar os coautores.");
            }

            for (String collaboratorEmail : collaboratorEmails) {
                if (collaboratorEmail == null || collaboratorEmail.isBlank()) {
                    continue;
                }

                Researcher collaborator = userGateway.findByEmail(collaboratorEmail.trim())
                        .orElseThrow(() -> new IllegalArgumentException("Coautor não encontrado: " + collaboratorEmail.trim()));
                paper.addCollaborator(collaborator);
            }
        }

        return paperGateway.save(paper);
    }
}
