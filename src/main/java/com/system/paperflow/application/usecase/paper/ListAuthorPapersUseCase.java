package com.system.paperflow.application.usecase.paper;

import java.util.List;

import com.system.paperflow.application.gateway.PaperGateway;
import com.system.paperflow.domain.entity.Paper;

public class ListAuthorPapersUseCase {

    private final PaperGateway paperGateway;

    public ListAuthorPapersUseCase(PaperGateway paperGateway) {
        this.paperGateway = paperGateway;
    }

    public List<Paper> execute(String authorEmail) {
        return paperGateway.findByAuthorEmail(authorEmail);
    }
}
