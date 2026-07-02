package com.system.paperflow.application.usecase.committee;

import com.system.paperflow.application.exception.InvalidCommitteeInvitationException;
import com.system.paperflow.application.factory.ReviewerCreator;
import com.system.paperflow.application.observer.committee.CommitteeInvitationEvent;
import com.system.paperflow.application.observer.committee.CommitteeInvitationEventType;
import com.system.paperflow.application.observer.committee.CommitteeInvitationPublisher;
import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.domain.entity.CommitteeInvitation;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.Topic;

import java.util.Set;

public class AcceptCommitteeInvitationUseCase {

    private final CommitteePersistence committeePersistence;
    private final CommitteeInvitationPublisher publisher;
    private final FindCommitteeInvitationByIdUseCase findInvitationByIdUseCase;
    private final ReviewerCreator reviewerCreator;

    public AcceptCommitteeInvitationUseCase(
            CommitteePersistence committeePersistence,
            CommitteeInvitationPublisher publisher
    ) {
        this.committeePersistence = committeePersistence;
        this.publisher = publisher;
        this.findInvitationByIdUseCase = new FindCommitteeInvitationByIdUseCase(committeePersistence);
        this.reviewerCreator = new ReviewerCreator();
    }

    public Researcher execute(String invitationId, String reviewerEmail, Set<Topic> expertiseAreas) {
        CommitteeInvitation invitation = findInvitationByIdUseCase.execute(invitationId);

        validateExpertiseAreas(expertiseAreas);
        invitation.acceptBy(reviewerEmail);

        Researcher reviewer = reviewerCreator.assignReviewerProfile(invitation.getInvitedResearcher(), expertiseAreas);

        committeePersistence.updateInvitationStatus(invitation);
        committeePersistence.saveReviewerExpertise(invitation.getId(), reviewer, expertiseAreas);
        notifyAccepted(invitation);

        return reviewer;
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
