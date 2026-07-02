package com.system.paperflow.presentation.context;

import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.domain.entity.Researcher;

public class ScreenContext {

    private Researcher currentUser;

    public void startSession(Researcher user) {
        this.currentUser = user;
    }

    public void endSession() {
        this.currentUser = null;
    }

    public Researcher currentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("Nenhum usuario logado.");
        }

        return currentUser;
    }
}
