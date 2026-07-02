package com.system.paperflow.application.observer;

import com.system.paperflow.application.observer.event.PaperReviewEvent;

public interface PaperReviewObserver {
    void update(PaperReviewEvent event);
}
