package com.system.paperflow.application.usecase.paper;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.PaperGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.ThematicArea;

import java.util.List;
import java.util.UUID;

public class SubmitPaperUseCase {

    private final EventManager manager;
    private final PaperGateway paperGateway;

    public SubmitPaperUseCase(EventManager manager, PaperGateway paperGateway) {
        this.manager = manager;
        this.paperGateway = paperGateway;
    }

    public Paper execute(Researcher user, UUID eventId, String title, String summary, List<String> thematicAreas) {

        if (!manager.hasEvent()) {
            throw new IllegalStateException("Nenhum evento ativo.");
        }

        Event event = manager.getCurrentEvent();

        if (!event.isOpenForSubmission()) {
            throw new IllegalStateException(
                    "O evento não está aberto para submissão."
            );
        }

        Paper paper = new Paper(title, summary, user, event);

        for (String area : thematicAreas) {
            paper.addThematicArea(new ThematicArea(area));
        }

        return paperGateway.save(paper);
    }
}
