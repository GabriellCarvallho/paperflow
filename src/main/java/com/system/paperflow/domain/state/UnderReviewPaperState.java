package com.system.paperflow.domain.state;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.PaperStatus;

public class UnderReviewPaperState implements PaperState {

    @Override
    public PaperStatus status() {
        return PaperStatus.UNDER_REVIEW;
    }

    @Override
    public void markAsAccepted(Paper paper) {
        paper.changeState(new AcceptedPaperState());
    }

    @Override
    public void markAsRejected(Paper paper) {
        paper.changeState(new RejectedPaperState());
    }
}
