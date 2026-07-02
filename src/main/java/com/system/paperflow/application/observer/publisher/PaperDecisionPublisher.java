package com.system.paperflow.application.observer.publisher;

import com.system.paperflow.application.observer.PaperReviewObserver;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.ArrayList;
import java.util.List;

public class PaperDecisionPublisher {

    private final List<PaperReviewObserver> observers = new ArrayList<>();

    public void addObserver(PaperReviewObserver observer) {
        observers.add(observer);
    }

    public void publish(Paper paper, List<ReviewAssignment> assignments) {
        for (PaperReviewObserver observer : observers) {
            observer.update(paper, assignments);
        }
    }
}
