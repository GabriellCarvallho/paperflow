package com.system.paperflow.presentation.context;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;

import java.util.UUID;

public class ScreenContext {

    private Researcher currentUser;
    private UUID selectedPaperId;

    public void startSession(Researcher user) {
        this.currentUser = user;
    }

    public void endSession() {
        this.currentUser = null;
        this.selectedPaperId = null;
    }

    public Researcher currentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("Nenhum usuario logado.");
        }

        return currentUser;
    }

    public void selectPaper(Paper paper) {
        this.selectedPaperId = paper.getId();
    }

    public UUID selectedPaperId() {
        if (selectedPaperId == null) {
            throw new IllegalStateException("Nenhum artigo selecionado.");
        }

        return selectedPaperId;
    }

    public void clearSelectedPaper() {
        this.selectedPaperId = null;
    }
}
