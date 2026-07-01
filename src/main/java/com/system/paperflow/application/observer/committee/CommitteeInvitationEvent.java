package com.system.paperflow.application.observer.committee;

import com.system.paperflow.domain.entity.CommitteeInvitation;

import java.time.LocalDateTime;

public class CommitteeInvitationEvent {

    private final CommitteeInvitationEventType type;
    private final CommitteeInvitation invitation;
    private final String message;
    private final LocalDateTime occurredAt;

    public CommitteeInvitationEvent(CommitteeInvitationEventType type, CommitteeInvitation invitation, String message) {
        this.type = type;
        this.invitation = invitation;
        this.message = message;
        this.occurredAt = LocalDateTime.now();
    }

    public CommitteeInvitationEventType getType() {
        return type;
    }

    public CommitteeInvitation getInvitation() {
        return invitation;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
