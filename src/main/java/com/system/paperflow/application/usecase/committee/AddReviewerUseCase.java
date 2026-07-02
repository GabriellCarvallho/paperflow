package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.UserGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Researcher;

public class AddReviewerUseCase {

    private final EventManager manager;
    private final UserGateway userGateway;

    public AddReviewerUseCase(EventManager manager, UserGateway userGateway) {
        this.manager = manager;
        this.userGateway = userGateway;
    }

    public void execute(Researcher coordinator, String reviewerEmail) {

        if (!manager.hasEvent()) {
            throw new IllegalStateException("Nenhum evento ativo.");
        }

        if (!coordinator.isCoordinator()) {
            throw new IllegalArgumentException("Apenas coordenadores podem adicionar revisores.");
        }

        Researcher reviewer = userGateway.findByEmail(reviewerEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Revisor não encontrado.")
                );

        Event event = manager.getCurrentEvent();

        event.addReviewer(reviewer);
    }
}