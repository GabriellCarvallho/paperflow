package com.system.paperflow.application.usecase.distribute;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.PaperGateway;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DistributePapersUseCase {

    private final PaperGateway paperGateway;
    private final EventManager manager;
    private final ReviewAssignmentGateway assignmentGateway;

    public DistributePapersUseCase(PaperGateway paperGateway, EventManager manager, ReviewAssignmentGateway assignmentGateway) {
        this.paperGateway = paperGateway;
        this.manager = manager;
        this.assignmentGateway = assignmentGateway;
    }

    public List<ReviewAssignment> execute(UUID eventId) {

        if (!manager.hasEvent()) {
            throw new IllegalStateException("Nenhum evento ativo.");
        }

        Event event = manager.getCurrentEvent();
        List<Paper> papers = paperGateway.findByEvent(event);

        List<Researcher> reviewers = event.getCommittee();
        List<ReviewAssignment> assignments = new ArrayList<>();

        if (papers.isEmpty() || reviewers.isEmpty()) {
            return assignments;
        }

        for (Paper paper : papers) {

            Researcher selected = reviewers.stream()
                    .filter(r -> !hasConflict(paper, r))
                    .max((r1, r2) ->
                            Integer.compare(
                                    compatibility(paper, r1),
                                    compatibility(paper, r2)
                            )
                    )
                    .orElseThrow(() ->
                            new IllegalStateException("Nenhum reviewer disponível")
                    );

            if (!assignmentGateway.exists(paper.getId(), selected.getEmail())) {
                ReviewAssignment assignment = assignmentGateway.save(new ReviewAssignment(paper, selected));
                assignments.add(assignment);
            }
        }

        return assignments;
    }

    private boolean hasConflict(Paper paper, Researcher reviewer) {

        if (paper.getAuthor().getEmail().equalsIgnoreCase(reviewer.getEmail())) {
            return true;
        }

        return paper.getCollaborators().stream().anyMatch(c -> c.getEmail().equalsIgnoreCase(reviewer.getEmail()));
    }

    private int compatibility(Paper paper, Researcher reviewer) {
        return (int) paper.getAreas().stream().filter(reviewer.getAreas()::contains).count();
    }
}
