package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.exception.InvalidCommitteeInvitationException;
import com.system.paperflow.application.exception.UnauthorizedCommitteeManagementException;
import com.system.paperflow.application.observer.committee.CommitteeInvitationEvent;
import com.system.paperflow.application.observer.committee.CommitteeInvitationEventType;
import com.system.paperflow.application.observer.committee.CommitteeInvitationPublisher;
import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.CommitteeInvitation;
import com.system.paperflow.domain.entity.Coordinator;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.User;

import java.util.UUID;

public class InviteReviewerUseCase {

    private final UserPersistence userPersistence;
    private final CommitteePersistence committeePersistence;
    private final CommitteeInvitationPublisher publisher;

    public InviteReviewerUseCase(
            UserPersistence userPersistence,
            CommitteePersistence committeePersistence,
            CommitteeInvitationPublisher publisher
    ) {
        this.userPersistence = userPersistence;
        this.committeePersistence = committeePersistence;
        this.publisher = publisher;
    }

    public CommitteeInvitation execute(String coordinatorEmail, String reviewerEmail) {
        validateCoordinator(coordinatorEmail);
        validateInvitedUser(reviewerEmail);
        validateNoPendingInvitation(reviewerEmail);

        CommitteeInvitation invitation = new CommitteeInvitation(
                UUID.randomUUID().toString(),
                coordinatorEmail,
                reviewerEmail
        );

        committeePersistence.saveInvitation(invitation);
        notifyCreated(invitation);

        return invitation;
    }

    private void validateCoordinator(String coordinatorEmail) {
        User user = userPersistence.findByEmail(coordinatorEmail)
                .orElseThrow(() -> new UnauthorizedCommitteeManagementException(
                        "Somente um coordenador cadastrado pode convidar revisores."
                ));

        if (!(user instanceof Coordinator)) {
            throw new UnauthorizedCommitteeManagementException(
                    "Somente um coordenador pode gerenciar convites do comite tecnico."
            );
        }
    }

    private void validateInvitedUser(String reviewerEmail) {
        User user = userPersistence.findByEmail(reviewerEmail)
                .orElseThrow(() -> new InvalidCommitteeInvitationException(
                        "O usuario convidado precisa estar previamente cadastrado."
                ));

        if (!(user instanceof Researcher)) {
            throw new InvalidCommitteeInvitationException(
                    "Apenas pesquisadores cadastrados podem ser convidados para revisao."
            );
        }
    }

    private void validateNoPendingInvitation(String reviewerEmail) {
        committeePersistence.findPendingInvitationByReviewerEmail(reviewerEmail)
                .ifPresent(invitation -> {
                    throw new InvalidCommitteeInvitationException(
                            "Ja existe um convite pendente para este usuario."
                    );
                });
    }

    private void notifyCreated(CommitteeInvitation invitation) {
        publisher.notifyObservers(new CommitteeInvitationEvent(
                CommitteeInvitationEventType.CREATED,
                invitation,
                "Convite criado para " + invitation.getReviewerEmail()
        ));
    }
}
