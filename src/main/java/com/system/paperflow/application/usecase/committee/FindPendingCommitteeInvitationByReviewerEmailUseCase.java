package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.exception.InvalidCommitteeInvitationException;
import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.domain.entity.CommitteeInvitation;

import java.util.Optional;

public class FindPendingCommitteeInvitationByReviewerEmailUseCase {

    private final CommitteePersistence committeePersistence;

    public FindPendingCommitteeInvitationByReviewerEmailUseCase(CommitteePersistence committeePersistence) {
        this.committeePersistence = committeePersistence;
    }

    public Optional<CommitteeInvitation> execute(String reviewerEmail) {
        if (reviewerEmail == null || reviewerEmail.trim().isEmpty()) {
            throw new InvalidCommitteeInvitationException("Informe o email do usuario convidado.");
        }

        return committeePersistence.findPendingInvitationByReviewerEmail(reviewerEmail);
    }
}
