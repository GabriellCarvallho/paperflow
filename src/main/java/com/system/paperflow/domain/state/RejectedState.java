package com.system.paperflow.domain.state;

import com.system.paperflow.domain.entity.Paper;

public class RejectedState implements PaperState {

    @Override
    public String getStatus() {
        return "Rejected";
    }

    @Override
    public void advance(Paper paper) {
        throw new IllegalStateException("Rejected is a final state.");
    }
}