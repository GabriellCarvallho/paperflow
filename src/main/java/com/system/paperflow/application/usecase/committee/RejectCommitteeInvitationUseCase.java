package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.exception.CommitteeInvitationNotFoundException;
import com.system.paperflow.application.exception.InvalidCommitteeInvitationException;
import com.system.paperflow.application.observer.committee.CommitteeInvitationEvent;
import com.system.paperflow.application.observer.committee.CommitteeInvitationEventType;
import com.system.paperflow.application.observer.committee.CommitteeInvitationPublisher;
import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.domain.entity.CommitteeInvitation;

public class RejectCommitteeInvitationUseCase {

    private final CommitteePersistence committeePersistence;
    private final CommitteeInvitationPublisher publisher;

    public RejectCommitteeInvitationUseCase(
            CommitteePersistence committeePersistence,
            CommitteeInvitationPublisher publisher
    ) {
        this.committeePersistence = committeePersistence;
        this.publisher = publisher;
    }

    public CommitteeInvitation execute(String invitationId, String reviewerEmail) {
        CommitteeInvitation invitation = committeePersistence.findInvitationById(invitationId)
                .orElseThrow(() -> new CommitteeInvitationNotFoundException("Convite nao encontrado."));

        validateInvitationOwner(invitation, reviewerEmail);

        invitation.reject();
        committeePersistence.updateInvitationStatus(invitation);
        notifyRejected(invitation);

        return invitation;
    }

    private void validateInvitationOwner(CommitteeInvitation invitation, String reviewerEmail) {
        if (!invitation.getReviewerEmail().equalsIgnoreCase(reviewerEmail)) {
            throw new InvalidCommitteeInvitationException(
                    "Este convite pertence a outro usuario."
            );
        }

        if (!invitation.isPending()) {
            throw new InvalidCommitteeInvitationException(
                    "Apenas convites pendentes podem ser rejeitados."
            );
        }
    }

    private void notifyRejected(CommitteeInvitation invitation) {
        publisher.notifyObservers(new CommitteeInvitationEvent(
                CommitteeInvitationEventType.REJECTED,
                invitation,
                "Convite rejeitado por " + invitation.getReviewerEmail()
        ));
    }
}
