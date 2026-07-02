package com.system.paperflow.application.usecase.review;

import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.observer.publisher.PaperDecisionPublisher;
import com.system.paperflow.domain.entity.*;
import com.system.paperflow.domain.entity.ReviewVerdict;

import java.util.List;
import java.util.UUID;

public class SubmitReviewUseCase {

    private final ReviewAssignmentGateway assignmentGateway;
    private final PaperDecisionPublisher publisher;

    public SubmitReviewUseCase(ReviewAssignmentGateway assignmentGateway, PaperDecisionPublisher publisher) {
        this.assignmentGateway = assignmentGateway;
        this.publisher = publisher;
    }

    public ReviewAssignment execute(UUID paperId, Researcher reviewer, String contribution, String criticism, ReviewVerdict reviewVerdict) {

        ReviewAssignment assignment = assignmentGateway
                .findByReviewerEmail(paperId, reviewer.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Artigo não atribuído a este revisor."));

        Review review = new Review(
                reviewer,
                contribution,
                criticism,
                reviewVerdict
        );

        assignment.finish(review);

        List<ReviewAssignment> assignments = assignmentGateway.findByPaperId(paperId);

        boolean allFinished = assignments.stream().allMatch(ReviewAssignment::isFinished);

        if (allFinished && assignments.size() >= 2) {
            boolean accepted = assignments.stream()
                    .map(ReviewAssignment::getReview)
                    .map(Review::getVerdict)
                    .allMatch(ReviewVerdict::isPositive);

            Paper paper = assignment.getPaper();

            if (accepted) {
                paper.markAsAccepted();
            } else {
                paper.markAsRejected();
            }

            publisher.publish(paper);
        }

        return assignment;
    }
}
