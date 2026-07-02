package com.system.paperflow.infrastructure.gateway;

import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryReviewAssignmentGateway implements ReviewAssignmentGateway {

    private final List<ReviewAssignment> assignments = new ArrayList<>();

    @Override
    public ReviewAssignment save(ReviewAssignment assignment) {
        assignments.removeIf(existing ->
                existing.getPaper().getId().equals(assignment.getPaper().getId())
                        && existing.getReviewer().getEmail().equalsIgnoreCase(assignment.getReviewer().getEmail())
        );
        assignments.add(assignment);
        return assignment;
    }

    @Override
    public List<ReviewAssignment> findByEvent(Event event) {
        return assignments.stream()
                .filter(assignment -> assignment.getPaper().getEvent().equals(event))
                .toList();
    }

    @Override
    public Optional<ReviewAssignment> findByReviewerEmail(UUID paperId, String reviewerEmail) {
        return assignments.stream()
                .filter(assignment -> assignment.getPaper().getId().equals(paperId))
                .filter(assignment -> assignment.getReviewer().getEmail().equalsIgnoreCase(reviewerEmail))
                .findFirst();
    }

    @Override
    public List<ReviewAssignment> findByPaperId(UUID paperId) {
        return assignments.stream()
                .filter(assignment -> assignment.getPaper().getId().equals(paperId))
                .toList();
    }

    @Override
    public boolean exists(UUID paperId, String reviewerEmail) {
        return findByReviewerEmail(paperId, reviewerEmail).isPresent();
    }
}
