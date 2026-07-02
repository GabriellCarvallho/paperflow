package com.system.paperflow.domain.entity;

import java.time.LocalDateTime;

import com.system.paperflow.domain.enums.Verdict;

public class Review {

    private final Researcher reviewer;
    private final String contribution;
    private final String criticism;
    private final Verdict verdict;
    private final LocalDateTime createdAt;

    public Review(Researcher reviewer, String contribution, String criticism, Verdict verdict) {
        this.reviewer = reviewer;
        this.contribution = contribution;
        this.criticism = criticism;
        this.verdict = verdict;
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

    public Verdict getVerdict() {
        return verdict;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
