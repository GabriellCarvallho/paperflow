package com.system.paperflow.domain.state;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.PaperStatus;

public class SubmittedPaperState implements PaperState {

    @Override
    public PaperStatus status() {
        return PaperStatus.SUBMITTED;
    }

    @Override
    public void markAsUnderReview(Paper paper) {
        paper.changeState(new UnderReviewPaperState());
    }
}
