package com.system.paperflow.presentation.console;

import com.system.paperflow.domain.entity.Researcher;

public class ConsoleSession {

    private Researcher currentUser;

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public Researcher currentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("Nenhum usuario autenticado.");
        }
        return currentUser;
    }

    public void login(Researcher researcher) {
        this.currentUser = researcher;
    }

    public void logout() {
        this.currentUser = null;
    }
}
