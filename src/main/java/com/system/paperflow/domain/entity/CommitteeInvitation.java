package com.system.paperflow.domain.entity;

import java.time.LocalDateTime;

import com.system.paperflow.domain.enums.InvitationStatus;

public class CommitteeInvitation {

    private final String id;
    private final Researcher coordinator;
    private final Researcher invitedResearcher;
    private InvitationStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime answeredAt;

    public CommitteeInvitation(String id, Researcher coordinator, Researcher invitedResearcher) {
        this(id, coordinator, invitedResearcher, InvitationStatus.PENDING, LocalDateTime.now(), null);
    }

    public CommitteeInvitation(
            String id,
            Researcher coordinator,
            Researcher invitedResearcher,
            InvitationStatus status,
            LocalDateTime createdAt,
            LocalDateTime answeredAt
    ) {
        this.id = id;
        this.coordinator = coordinator;
        this.invitedResearcher = invitedResearcher;
        this.status = status == null ? InvitationStatus.PENDING : status;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.answeredAt = answeredAt;
    }

    public String getId() {
        return id;
    }

    public Researcher getCoordinator() {
        return coordinator;
    }

    public Researcher getInvitedResearcher() {
        return invitedResearcher;
    }

    public String getCoordinatorEmail() {
        return coordinator.getEmail();
    }

    public String getReviewerEmail() {
        return invitedResearcher.getEmail();
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }

    public boolean isPending() {
        return InvitationStatus.PENDING.equals(status);
    }

    public void acceptBy(String reviewerEmail) {
        ensureCanBeAnsweredBy(reviewerEmail, "aceito");
        this.status = InvitationStatus.ACCEPTED;
        this.answeredAt = LocalDateTime.now();
    }

    public void rejectBy(String reviewerEmail) {
        ensureCanBeAnsweredBy(reviewerEmail, "rejeitado");
        this.status = InvitationStatus.REJECTED;
        this.answeredAt = LocalDateTime.now();
    }

    public void accept() {
        acceptBy(invitedResearcher.getEmail());
    }

    public void reject() {
        rejectBy(invitedResearcher.getEmail());
    }

    public void ensureCanBeAnsweredBy(String reviewerEmail, String action) {
        String normalizedReviewerEmail = reviewerEmail.trim().toLowerCase();

        if (!invitedResearcher.getEmail().equalsIgnoreCase(normalizedReviewerEmail)) {
            throw new IllegalArgumentException("Este convite pertence a outro usuario.");
        }

        if (!isPending()) {
            throw new IllegalStateException(
                    "O convite nao pode ser " + action + " porque seu status atual e " + status + "."
            );
        }
    }
}
