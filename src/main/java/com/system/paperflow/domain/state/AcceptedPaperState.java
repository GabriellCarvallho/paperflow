package com.system.paperflow.domain.state;

import com.system.paperflow.domain.entity.PaperStatus;

public class AcceptedPaperState implements PaperState {

    @Override
    public PaperStatus status() {
        return PaperStatus.ACCEPTED;
    }
}
