package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.observer.committee.CommitteeInvitationEvent;
import com.system.paperflow.application.observer.committee.CommitteeInvitationEventType;
import com.system.paperflow.application.observer.committee.CommitteeInvitationPublisher;
import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.domain.entity.CommitteeInvitation;

public class RejectCommitteeInvitationUseCase {

    private final CommitteePersistence committeePersistence;
    private final CommitteeInvitationPublisher publisher;
    private final FindCommitteeInvitationByIdUseCase findInvitationByIdUseCase;

    public RejectCommitteeInvitationUseCase(
            CommitteePersistence committeePersistence,
            CommitteeInvitationPublisher publisher
    ) {
        this.committeePersistence = committeePersistence;
        this.publisher = publisher;
        this.findInvitationByIdUseCase = new FindCommitteeInvitationByIdUseCase(committeePersistence);
    }

    public CommitteeInvitation execute(String invitationId, String reviewerEmail) {
        CommitteeInvitation invitation = findInvitationByIdUseCase.execute(invitationId);

        invitation.rejectBy(reviewerEmail);
        committeePersistence.updateInvitationStatus(invitation);
        notifyRejected(invitation);

        return invitation;
    }

    private void notifyRejected(CommitteeInvitation invitation) {
        publisher.notifyObservers(new CommitteeInvitationEvent(
                CommitteeInvitationEventType.REJECTED,
                invitation,
                "Convite rejeitado por " + invitation.getReviewerEmail()
        ));
    }
}
