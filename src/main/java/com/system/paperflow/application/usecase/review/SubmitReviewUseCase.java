package com.system.paperflow.application.usecase.review;

import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.domain.entity.*;
import com.system.paperflow.domain.enums.Verdict;
import com.system.paperflow.commons.StringUtils;

import java.util.UUID;

public class SubmitReviewUseCase {

    private final ReviewAssignmentGateway assignmentGateway;

    public SubmitReviewUseCase(ReviewAssignmentGateway assignmentGateway) {
        this.assignmentGateway = assignmentGateway;
    }

    public ReviewAssignment execute(UUID paperId, Researcher reviewer, String contribution, String criticism, Verdict verdict) {

        ReviewAssignment assignment = assignmentGateway.findByReviewerEmail(paperId, reviewer.getEmail()).orElseThrow(() ->
                        new IllegalArgumentException("Artigo não atribuído a este revisor."));

        Review review = new Review(
                reviewer,
                contribution,
                criticism,
                verdict
        );

//        assignment.finish(review);
//        assignment.getPaper().markAsCompleted();
        return assignment;
    }
}