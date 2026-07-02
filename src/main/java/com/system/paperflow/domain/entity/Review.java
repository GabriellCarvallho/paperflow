package com.system.paperflow.domain.entity;

import java.time.LocalDateTime;

public class Review {

    private final Researcher reviewer;
    private final String contribution;
    private final String criticism;
    private final ReviewVerdict reviewVerdict;
    private final LocalDateTime createdAt;

    public Review(Researcher reviewer, String contribution, String criticism, ReviewVerdict reviewVerdict) {
        this.reviewer = reviewer;
        this.contribution = contribution;
        this.criticism = criticism;
        this.reviewVerdict = reviewVerdict;
        this.createdAt = LocalDateTime.now();
    }

    public Researcher getReviewer() {
        return reviewer;
    }

    public String getContribution() {
        return contribution;
    }

    public String getCriticism() {
        return criticism;
    }

    public ReviewVerdict getVerdict() {
        return reviewVerdict;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
