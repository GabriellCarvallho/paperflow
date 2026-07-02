package com.system.paperflow.domain.entity;

public class ReviewAssignment {

    private final Paper paper;
    private final Researcher reviewer;
    private Review review;

    public ReviewAssignment(Paper paper, Researcher reviewer) {
        this.paper = paper;
        this.reviewer = reviewer;
    }

    public Paper getPaper() {
        return paper;
    }

    public Researcher getReviewer() {
        return reviewer;
    }

    public Review getReview() {
        return review;
    }
}
