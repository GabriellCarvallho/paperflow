package com.system.paperflow.domain.entity;

public class ReviewAssignment {

    private final Paper paper;
    private final Researcher reviewer;
    private Review review;

    public ReviewAssignment(Paper paper, Researcher reviewer) {
        this.paper = paper;
        this.reviewer = reviewer;
    }

    public void finish(Review review) {
        if (this.review != null) {
            throw new IllegalStateException("Este artigo já foi avaliado por este revisor.");
        }

        this.review = review;
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

    public Review getReview() {
        return review;
    }
}
