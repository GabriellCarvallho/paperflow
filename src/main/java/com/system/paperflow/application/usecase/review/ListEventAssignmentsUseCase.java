package com.system.paperflow.application.usecase.review;

import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.List;

public class ListEventAssignmentsUseCase {

    private final ReviewAssignmentGateway assignmentGateway;

    public ListEventAssignmentsUseCase(ReviewAssignmentGateway assignmentGateway) {
        this.assignmentGateway = assignmentGateway;
    }

    public List<ReviewAssignment> execute(Event event) {
        return assignmentGateway.findByEvent(event);
    }
}
