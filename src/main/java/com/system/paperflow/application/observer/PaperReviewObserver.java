package com.system.paperflow.application.observer;

import com.system.paperflow.domain.entity.Paper;

public interface PaperReviewObserver {
    void update(Paper paper);
}
