package com.system.paperflow.domain.entity;

import java.time.LocalDateTime;

import com.system.paperflow.domain.enums.InvitationStatus;

public class CommitteeInvitation {

    private final String id;
    private final Coordinator coordinator;
    private final User invitedUser;
    private InvitationStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime answeredAt;

    public CommitteeInvitation(String id, Coordinator coordinator, User invitedUser) {
        this(id, coordinator, invitedUser, InvitationStatus.PENDING, LocalDateTime.now(), null);
    }

    public CommitteeInvitation(
            String id,
            Coordinator coordinator,
            User invitedUser,
            InvitationStatus status,
            LocalDateTime createdAt,
            LocalDateTime answeredAt
    ) {
        this.id = validateText(id, "identificador do convite");
        this.coordinator = validateCoordinator(coordinator);
        this.invitedUser = validateInvitedUser(invitedUser);
        this.status = status == null ? InvitationStatus.PENDING : status;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.answeredAt = answeredAt;
    }

    public String getId() {
        return id;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public User getInvitedUser() {
        return invitedUser;
    }

    public String getCoordinatorEmail() {
        return coordinator.getEmail();
    }

    public String getReviewerEmail() {
        return invitedUser.getEmail();
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
        acceptBy(invitedUser.getEmail());
    }

    public void reject() {
        rejectBy(invitedUser.getEmail());
    }

    public void ensureCanBeAnsweredBy(String reviewerEmail, String action) {
        String normalizedReviewerEmail = validateEmail(reviewerEmail, "email do usuario que responde ao convite");

        if (!invitedUser.getEmail().equalsIgnoreCase(normalizedReviewerEmail)) {
            throw new IllegalArgumentException("Este convite pertence a outro usuario.");
        }

        if (!isPending()) {
            throw new IllegalStateException(
                    "O convite nao pode ser " + action + " porque seu status atual e " + status + "."
            );
        }
    }

    private Coordinator validateCoordinator(Coordinator coordinator) {
        if (coordinator == null) {
            throw new IllegalArgumentException("O convite precisa estar associado a um coordenador.");
        }

        return coordinator;
    }

    private User validateInvitedUser(User invitedUser) {
        if (invitedUser == null) {
            throw new IllegalArgumentException("O convite precisa estar associado a um usuario convidado.");
        }

        return invitedUser;
    }

    private String validateText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("O campo " + fieldName + " e obrigatorio.");
        }

        return value.trim();
    }

    private String validateEmail(String value, String fieldName) {
        String email = validateText(value, fieldName).toLowerCase();

        if (!email.contains("@")) {
            throw new IllegalArgumentException("O campo " + fieldName + " deve conter um email valido.");
        }

        return email;
    }
}
