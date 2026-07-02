package com.system.paperflow.domain.state;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.PaperStatus;

public interface PaperState {

    PaperStatus status();

    default void markAsUnderReview(Paper paper) {
        throw new IllegalStateException("O artigo nao pode ir para revisao a partir do status " + status() + ".");
    }

    default void markAsAccepted(Paper paper) {
        throw new IllegalStateException("O artigo nao pode ser aceito a partir do status " + status() + ".");
    }

    default void markAsRejected(Paper paper) {
        throw new IllegalStateException("O artigo nao pode ser rejeitado a partir do status " + status() + ".");
    }
}
