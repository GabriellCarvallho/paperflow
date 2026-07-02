package com.system.paperflow.application.gateway;

import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewAssignmentGateway {

    ReviewAssignment save(ReviewAssignment assignment);

    List<ReviewAssignment> findByEvent(Event event);

    Optional<ReviewAssignment> findByReviewerEmail(UUID paperId, String reviewerEmail);

    List<ReviewAssignment> findByPaperId(UUID paperId);

    boolean exists(UUID paperId, String reviewerEmail);
}
