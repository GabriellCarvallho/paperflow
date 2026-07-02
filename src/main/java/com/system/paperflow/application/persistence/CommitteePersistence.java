package com.system.paperflow.application.persistence;

import com.system.paperflow.domain.entity.CommitteeInvitation;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.Topic;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommitteePersistence {

    void saveInvitation(CommitteeInvitation invitation);

    Optional<CommitteeInvitation> findInvitationById(String invitationId);

    Optional<CommitteeInvitation> findPendingInvitationByReviewerEmail(String reviewerEmail);

    void updateInvitationStatus(CommitteeInvitation invitation);

    void saveReviewerExpertise(String invitationId, Researcher reviewer, Set<Topic> expertiseAreas);

    List<CommitteeInvitation> findAllInvitations();

    List<Researcher> findAcceptedReviewers();
}
