package com.system.paperflow.application.strategy.distribution;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;

import java.util.List;

public class CompatibilityDistributionStrategy implements PaperDistributionStrategy {

    @Override
    public List<Researcher> selectReviewers(Paper paper, List<Researcher> reviewers, int reviewerLimit) {
        return reviewers.stream()
                .filter(reviewer -> !hasConflict(paper, reviewer))
                .sorted((r1, r2) -> Integer.compare(
                        compatibility(paper, r2),
                        compatibility(paper, r1)
                ))
                .limit(reviewerLimit)
                .toList();
    }

    private boolean hasConflict(Paper paper, Researcher reviewer) {
        if (paper.getAuthor().getEmail().equalsIgnoreCase(reviewer.getEmail())) {
            return true;
        }

        return paper.getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getEmail().equalsIgnoreCase(reviewer.getEmail()));
    }

    private int compatibility(Paper paper, Researcher reviewer) {
        return (int) paper.getAreas().stream()
                .filter(reviewer.getAreas()::contains)
                .count();
    }
}
