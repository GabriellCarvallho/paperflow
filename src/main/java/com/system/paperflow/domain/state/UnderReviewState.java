package com.system.paperflow.domain.state;

import com.system.paperflow.domain.entity.Paper;

public class UnderReviewState implements PaperState {

    @Override
    public String getStatus() {
        return "Under Review";
    }

    @Override
    public void advance(Paper paper) {
        throw new IllegalStateException(
            "A paper under review must be explicitly accepted or rejected.");
    }

    public void accept(Paper paper) {
        paper.setState(new AcceptedState());
    }

    public void reject(Paper paper) {
        paper.setState(new RejectedState());
    }
}