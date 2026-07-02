package com.system.paperflow.application.usecase.paper;

import java.util.List;

import com.system.paperflow.application.gateway.PaperGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;

public class ListEventPapersUseCase {

    private final PaperGateway paperGateway;

    public ListEventPapersUseCase(PaperGateway paperGateway) {
        this.paperGateway = paperGateway;
    }

    public List<Paper> execute(Event event) {
        return paperGateway.findByEvent(event);
    }
}
