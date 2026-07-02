package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.UserGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.ThematicArea;

import java.util.List;

public class AddReviewerUseCase {

    private final EventManager manager;
    private final UserGateway userGateway;

    public AddReviewerUseCase(EventManager manager, UserGateway userGateway) {
        this.manager = manager;
        this.userGateway = userGateway;
    }

    public Researcher execute(Researcher coordinator, String reviewerEmail) {
        return execute(coordinator, reviewerEmail, List.of());
    }

    public Researcher execute(Researcher coordinator, String reviewerEmail, List<String> expertiseAreas) {

        if (!manager.hasEvent()) {
            throw new IllegalStateException("Nenhum evento ativo.");
        }

        if (!coordinator.isCoordinator()) {
            throw new IllegalArgumentException("Apenas coordenadores podem adicionar revisores.");
        }

        Researcher reviewer = userGateway.findByEmail(reviewerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Revisor não encontrado."));

        Event event = manager.getCurrentEvent();

        if (expertiseAreas != null) {
            for (String areaName : expertiseAreas) {
                if (areaName != null && !areaName.isBlank()) {
                    ThematicArea area = new ThematicArea(areaName.trim());

                    if (!event.getThematicAreas().contains(area)) {
                        throw new IllegalArgumentException("Área temática não cadastrada no evento: " + areaName.trim());
                    }

                    reviewer.addThematicArea(area);
                }
            }
        }

        if (reviewer.getAreas().isEmpty()) {
            throw new IllegalArgumentException("Informe pelo menos uma área de expertise do revisor.");
        }

        event.addReviewer(reviewer);
        return reviewer;
    }
}
