package com.system.paperflow.domain.entity;

import com.system.paperflow.domain.enums.InvitationStatus;

import java.time.LocalDateTime;

public class CommitteeInvitation {

    private final String id;
    private final String coordinatorEmail;
    private final String reviewerEmail;
    private InvitationStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime answeredAt;

    public CommitteeInvitation(String id, String coordinatorEmail, String reviewerEmail) {
        this(id, coordinatorEmail, reviewerEmail, InvitationStatus.PENDING, LocalDateTime.now(), null);
    }

    public CommitteeInvitation(
            String id,
            String coordinatorEmail,
            String reviewerEmail,
            InvitationStatus status,
            LocalDateTime createdAt,
            LocalDateTime answeredAt
    ) {
        this.id = validateText(id, "identificador do convite");
        this.coordinatorEmail = validateEmail(coordinatorEmail, "email do coordenador");
        this.reviewerEmail = validateEmail(reviewerEmail, "email do usuario convidado");
        this.status = status == null ? InvitationStatus.PENDING : status;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.answeredAt = answeredAt;
    }

    public String getId() {
        return id;
    }

    public String getCoordinatorEmail() {
        return coordinatorEmail;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
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

    public void accept() {
        ensurePending("aceito");
        this.status = InvitationStatus.ACCEPTED;
        this.answeredAt = LocalDateTime.now();
    }

    public void reject() {
        ensurePending("rejeitado");
        this.status = InvitationStatus.REJECTED;
        this.answeredAt = LocalDateTime.now();
    }

    private void ensurePending(String action) {
        if (!isPending()) {
            throw new IllegalStateException("O convite nao pode ser " + action + " porque seu status atual e " + status + ".");
        }
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
