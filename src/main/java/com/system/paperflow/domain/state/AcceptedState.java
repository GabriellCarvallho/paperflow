package com.system.paperflow.domain.state;

import com.system.paperflow.domain.entity.Paper;

public class AcceptedState implements PaperState {

    @Override
    public String getStatus() {
        return "Accepted";
    }

    @Override
    public void advance(Paper paper) {
        throw new IllegalStateException("Accepted is a final state.");
    }
}