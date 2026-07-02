package com.system.paperflow.application.usecase.distribute;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.PaperGateway;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.strategy.distribution.PaperDistributionStrategy;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DistributePapersUseCase {

    public static final int REVIEWERS_PER_PAPER = 2;

    private final PaperGateway paperGateway;
    private final EventManager manager;
    private final ReviewAssignmentGateway assignmentGateway;
    private PaperDistributionStrategy distributionStrategy;

    public DistributePapersUseCase(
            PaperGateway paperGateway,
            EventManager manager,
            ReviewAssignmentGateway assignmentGateway,
            PaperDistributionStrategy distributionStrategy
    ) {
        this.paperGateway = paperGateway;
        this.manager = manager;
        this.assignmentGateway = assignmentGateway;
        this.distributionStrategy = distributionStrategy;
    }

    public void setStrategy(PaperDistributionStrategy distributionStrategy) {
        if (distributionStrategy == null) {
            throw new IllegalArgumentException("A estrategia de distribuicao nao pode ser nula.");
        }

        this.distributionStrategy = distributionStrategy;
    }

    public List<ReviewAssignment> execute(UUID eventId) {

        if (!manager.hasEvent()) {
            throw new IllegalStateException("Nenhum evento ativo.");
        }

        Event event = manager.getCurrentEvent();

        if (!event.getId().equals(eventId)) {
            throw new IllegalArgumentException("Evento informado não é o evento ativo.");
        }

        List<Paper> papers = paperGateway.findByEvent(event);
        List<Researcher> reviewers = event.getCommittee();
        List<ReviewAssignment> assignments = new ArrayList<>();

        if (papers.isEmpty() || reviewers.isEmpty()) {
            return assignments;
        }

        for (Paper paper : papers) {

            List<Researcher> availableReviewers = reviewers.stream()
                    .filter(reviewer -> !assignmentGateway.exists(paper.getId(), reviewer.getEmail()))
                    .toList();

            List<Researcher> selectedReviewers = distributionStrategy.selectReviewers(
                    paper,
                    availableReviewers,
                    REVIEWERS_PER_PAPER
            );

            if (selectedReviewers.size() < REVIEWERS_PER_PAPER) {
                throw new IllegalStateException(
                        "Não há revisores suficientes para o artigo: " + paper.getTitle()
                );
            }

            for (Researcher reviewer : selectedReviewers) {
                ReviewAssignment assignment = assignmentGateway.save(
                        new ReviewAssignment(paper, reviewer)
                );

                assignments.add(assignment);
            }

            paper.markAsUnderReview();
            paperGateway.save(paper);
        }

        return assignments;
    }
}
