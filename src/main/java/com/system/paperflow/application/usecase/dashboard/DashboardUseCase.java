package com.system.paperflow.application.usecase.dashboard;

import com.system.paperflow.application.persistence.PaperPersistence;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.dashboard.DashboardData;
import com.system.paperflow.domain.entity.Paper;
import java.util.List;

public class DashboardUseCase {

    private final PaperPersistence paperPersistence;

    public DashboardUseCase(PaperPersistence paperPersistence) {
        this.paperPersistence = paperPersistence;
    }

    public DashboardData execute() {
        List<Paper> papers = paperPersistence.findAll();

        int submitted = papers.size();
        int evaluated = (int) papers.stream()
                .filter(p -> p.getStatus().equals("Accepted") || p.getStatus().equals("Rejected"))
                .count();
        int pending = submitted - evaluated;

        return new DashboardData(submitted, 0, evaluated, pending);
    }
}
