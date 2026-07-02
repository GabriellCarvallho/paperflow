package com.system.paperflow.application.observer;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.List;

public interface PaperReviewObserver {
    void update(Paper paper, List<ReviewAssignment> assignments);
}
