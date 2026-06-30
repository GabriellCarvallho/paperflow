package com.system.paperflow.application.dashboard;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardService implements DashboardObserver {

    private final List<Paper> papers;
    private final List<Researcher> reviewers;
    private DashboardDTO currentSnapshot;

    public DashboardService(List<Paper> papers, List<Researcher> reviewers) {
        this.papers = papers;
        this.reviewers = reviewers;
        refreshSnapshot();
    }

    @Override
    public void update() {
        refreshSnapshot();
    }

    public DashboardDTO generateDashboard() {
        return currentSnapshot;
    }

    private void refreshSnapshot() {
        int submitted = papers.size();
        int reviewersCount = reviewers.size();
        int evaluated = countByStatus("Accepted") + countByStatus("Rejected");
        int pending = submitted - evaluated;

        this.currentSnapshot = new DashboardDTO(
            submitted, reviewersCount, evaluated, pending, pendingByReviewer());
    }

    private int countByStatus(String status) {
        int count = 0;
        for (Paper paper : papers) {
            if (paper.getStatus().equals(status)) {
                count++;
            }
        }
        return count;
    }

    private Map<Paper, String> pendingByReviewer() {
        Map<Paper, String> map = new HashMap<>();
        for (Paper paper : papers) {
            if (paper.getStatus().equals("Under Review")) {
                map.put(paper, "To be defined");
            }
        }
        return map;
    }
}