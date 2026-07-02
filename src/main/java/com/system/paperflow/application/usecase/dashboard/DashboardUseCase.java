package com.system.paperflow.application.usecase.dashboard;

import com.system.paperflow.domain.dashboard.DashboardData;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import java.util.List;

public class DashboardUseCase {

    public DashboardData execute(List<Paper> papers, List<Researcher> reviewers) {
        int submitted = papers.size();
        int reviewersCount = reviewers.size();
        int evaluated = (int) papers.stream()
                .filter(p -> p.getStatus().equals("Accepted")
                          || p.getStatus().equals("Rejected"))
                .count();
        int pending = submitted - evaluated;

        return new DashboardData(submitted, reviewersCount, evaluated, pending);
    }
}
