package com.system.paperflow.domain.entity;

public class ReviewAssignment {

    private final Paper paper;
    private final Researcher reviewer;
    private Review review;

    public ReviewAssignment(Paper paper, Researcher reviewer) {
        this.paper = paper;
        this.reviewer = reviewer;
    }

    public boolean isFinished() {
        return review != null;
    }

    public Paper getPaper() {
        return paper;
    }

    public Researcher getReviewer() {
        return reviewer;
    }

    public void finish(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("A revisão não pode ser vazia.");
        }

        if (isFinished()) {
            throw new IllegalStateException("Esta revisão já foi concluída.");
        }

        this.review = review;
    }

    public Review getReview() {
        return review;
    }
}
