package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.exception.CommitteeInvitationNotFoundException;
import com.system.paperflow.application.exception.InvalidCommitteeInvitationException;
import com.system.paperflow.application.observer.committee.CommitteeInvitationEvent;
import com.system.paperflow.application.observer.committee.CommitteeInvitationEventType;
import com.system.paperflow.application.observer.committee.CommitteeInvitationPublisher;
import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.CommitteeInvitation;
import com.system.paperflow.domain.entity.Reviewer;
import com.system.paperflow.domain.entity.Topic;
import com.system.paperflow.domain.entity.User;

import java.util.Set;

public class AcceptCommitteeInvitationUseCase {

    private final UserPersistence userPersistence;
    private final CommitteePersistence committeePersistence;
    private final CommitteeInvitationPublisher publisher;

    public AcceptCommitteeInvitationUseCase(
            UserPersistence userPersistence,
            CommitteePersistence committeePersistence,
            CommitteeInvitationPublisher publisher
    ) {
        this.userPersistence = userPersistence;
        this.committeePersistence = committeePersistence;
        this.publisher = publisher;
    }

    public Reviewer execute(String invitationId, String reviewerEmail, Set<Topic> expertiseAreas) {
        CommitteeInvitation invitation = committeePersistence.findInvitationById(invitationId)
                .orElseThrow(() -> new CommitteeInvitationNotFoundException("Convite nao encontrado."));

        validateInvitationOwner(invitation, reviewerEmail);
        validateExpertiseAreas(expertiseAreas);

        User user = userPersistence.findByEmail(reviewerEmail)
                .orElseThrow(() -> new InvalidCommitteeInvitationException(
                        "O usuario convidado nao foi encontrado no cadastro."
                ));

        invitation.accept();

        Reviewer reviewer = new Reviewer(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getInstitution(),
                expertiseAreas
        );

        committeePersistence.updateInvitationStatus(invitation);
        committeePersistence.saveReviewerExpertise(invitation.getId(), reviewer, expertiseAreas);
        notifyAccepted(invitation);

        return reviewer;
    }

    private void validateInvitationOwner(CommitteeInvitation invitation, String reviewerEmail) {
        if (!invitation.getReviewerEmail().equalsIgnoreCase(reviewerEmail)) {
            throw new InvalidCommitteeInvitationException(
                    "Este convite pertence a outro usuario."
            );
        }

        if (!invitation.isPending()) {
            throw new InvalidCommitteeInvitationException(
                    "Apenas convites pendentes podem ser aceitos."
            );
        }
    }

    private void validateExpertiseAreas(Set<Topic> expertiseAreas) {
        if (expertiseAreas == null || expertiseAreas.isEmpty()) {
            throw new InvalidCommitteeInvitationException(
                    "O revisor deve informar pelo menos uma area de expertise ao aceitar o convite."
            );
        }
    }

    private void notifyAccepted(CommitteeInvitation invitation) {
        publisher.notifyObservers(new CommitteeInvitationEvent(
                CommitteeInvitationEventType.ACCEPTED,
                invitation,
                "Convite aceito por " + invitation.getReviewerEmail()
        ));
    }
}
