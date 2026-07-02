package com.system.paperflow.application.observer.publisher;

import com.system.paperflow.application.observer.PaperReviewObserver;
import com.system.paperflow.application.observer.event.PaperReviewEvent;

import java.util.ArrayList;
import java.util.List;

public class PaperReviewPublisher {

    private final List<PaperReviewObserver> observers = new ArrayList<>();

    public void addObserver(PaperReviewObserver observer) {
        observers.add(observer);
    }

    public void publish(PaperReviewEvent event) {
        for (PaperReviewObserver observer : observers) {
            observer.update(event);
        }
    }
}
