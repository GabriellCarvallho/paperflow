package com.system.paperflow.application.usecase.committee;

import java.util.UUID;

import com.system.paperflow.application.exception.InvalidCommitteeInvitationException;
import com.system.paperflow.application.exception.UnauthorizedCommitteeManagementException;
import com.system.paperflow.application.observer.committee.CommitteeInvitationEvent;
import com.system.paperflow.application.observer.committee.CommitteeInvitationEventType;
import com.system.paperflow.application.observer.committee.CommitteeInvitationPublisher;
import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.application.usecase.user.FindUserByEmailUseCase;
import com.system.paperflow.domain.entity.CommitteeInvitation;
import com.system.paperflow.domain.entity.Coordinator;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.User;

public class InviteReviewerUseCase {

    private final CommitteePersistence committeePersistence;
    private final CommitteeInvitationPublisher publisher;
    private final FindUserByEmailUseCase findUserByEmailUseCase;
    private final FindPendingCommitteeInvitationByReviewerEmailUseCase findPendingInvitationUseCase;

    public InviteReviewerUseCase(
            UserPersistence userPersistence,
            CommitteePersistence committeePersistence,
            CommitteeInvitationPublisher publisher
    ) {
        this.committeePersistence = committeePersistence;
        this.publisher = publisher;
        this.findUserByEmailUseCase = new FindUserByEmailUseCase(userPersistence);
        this.findPendingInvitationUseCase = new FindPendingCommitteeInvitationByReviewerEmailUseCase(committeePersistence);
    }

    public CommitteeInvitation execute(String coordinatorEmail, String reviewerEmail) {
        Coordinator coordinator = findCoordinator(coordinatorEmail);
        User invitedUser = findInvitedResearcher(reviewerEmail);
        validateNoPendingInvitation(invitedUser.getEmail());

        CommitteeInvitation invitation = new CommitteeInvitation(
                UUID.randomUUID().toString(),
                coordinator,
                invitedUser
        );

        committeePersistence.saveInvitation(invitation);
        notifyCreated(invitation);

        return invitation;
    }

    private Coordinator findCoordinator(String coordinatorEmail) {
        User user = findUserByEmailUseCase.execute(coordinatorEmail);

        if (!(user instanceof Coordinator coordinator)) {
            throw new UnauthorizedCommitteeManagementException(
                    "Somente um coordenador pode gerenciar convites do comite tecnico."
            );
        }

        return coordinator;
    }

    private User findInvitedResearcher(String reviewerEmail) {
        User user = findUserByEmailUseCase.execute(reviewerEmail);

        if (!(user instanceof Researcher)) {
            throw new InvalidCommitteeInvitationException(
                    "Apenas pesquisadores cadastrados podem ser convidados para revisao."
            );
        }

        return user;
    }

    private void validateNoPendingInvitation(String reviewerEmail) {
        findPendingInvitationUseCase.execute(reviewerEmail)
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
