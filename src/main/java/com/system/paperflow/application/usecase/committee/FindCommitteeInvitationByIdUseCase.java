package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.exception.CommitteeInvitationNotFoundException;
import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.domain.entity.CommitteeInvitation;

public class FindCommitteeInvitationByIdUseCase {

    private final CommitteePersistence committeePersistence;

    public FindCommitteeInvitationByIdUseCase(CommitteePersistence committeePersistence) {
        this.committeePersistence = committeePersistence;
    }

    public CommitteeInvitation execute(String invitationId) {
        return committeePersistence.findInvitationById(invitationId)
                .orElseThrow(() -> new CommitteeInvitationNotFoundException("Convite nao encontrado."));
    }
}
