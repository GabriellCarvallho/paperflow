package com.system.paperflow.application.usecase.review;

import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.Review;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.domain.enums.Verdict;

import java.util.UUID;

public class SubmitReviewUseCase {

    private final ReviewAssignmentGateway assignmentGateway;

    public SubmitReviewUseCase(ReviewAssignmentGateway assignmentGateway) {
        this.assignmentGateway = assignmentGateway;
    }

    public ReviewAssignment execute(UUID paperId, Researcher reviewer, String contribution, String criticism, Verdict verdict) {

        ReviewAssignment assignment = assignmentGateway.findByReviewerEmail(paperId, reviewer.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("Artigo não atribuído a este revisor."));

        if (contribution == null || contribution.isBlank()) {
            throw new IllegalArgumentException("Informe as contribuições do artigo.");
        }

        if (criticism == null || criticism.isBlank()) {
            throw new IllegalArgumentException("Informe os pontos de crítica do artigo.");
        }

        Review review = new Review(reviewer, contribution.trim(), criticism.trim(), verdict);
        assignment.finish(review);

        if (verdict == Verdict.ACCEPTED || verdict == Verdict.WEAKLY_ACCEPTED) {
            assignment.getPaper().accept();
        } else {
            assignment.getPaper().reject();
        }

        assignmentGateway.save(assignment);
        return assignment;
    }
}
