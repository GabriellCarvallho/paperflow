package com.system.paperflow.application.usecase.thematic;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.domain.entity.*;

import java.util.UUID;

public class CreateThematicAreaUseCase {

    private final EventManager manager;

    public CreateThematicAreaUseCase(EventManager manager) {
        this.manager = manager;
    }

    public ThematicArea execute(Researcher researcher, UUID eventId, String areaName) {

        if (!manager.hasEvent()) {
            throw new IllegalStateException("Nenhum evento ativo.");
        }

        if (!researcher.isCoordinator()) throw new IllegalArgumentException("Apenas coordenadores podem criar areas tematicas");

        Event event = manager.getCurrentEvent();

        ThematicArea area = new ThematicArea(areaName);

        event.addThematicArea(area);
        return area;
    }
}
