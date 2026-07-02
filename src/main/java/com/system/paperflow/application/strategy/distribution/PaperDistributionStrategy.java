package com.system.paperflow.application.strategy.distribution;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;

import java.util.List;

public interface PaperDistributionStrategy {

    List<Researcher> selectReviewers(Paper paper, List<Researcher> reviewers, int reviewerLimit);
}
