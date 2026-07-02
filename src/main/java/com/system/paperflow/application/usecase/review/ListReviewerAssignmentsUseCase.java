package com.system.paperflow.application.usecase.review;

import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.List;

public class ListReviewerAssignmentsUseCase {

    private final ReviewAssignmentGateway assignmentGateway;

    public ListReviewerAssignmentsUseCase(ReviewAssignmentGateway assignmentGateway) {
        this.assignmentGateway = assignmentGateway;
    }

    public List<ReviewAssignment> execute(Event event, Researcher reviewer) {
        return assignmentGateway.findByEvent(event).stream()
                .filter(assignment -> assignment.getReviewer().equals(reviewer))
                .toList();
    }
}
