package com.system.paperflow.application.dashboard;

import com.system.paperflow.domain.entity.Paper;
import java.util.Map;

public class DashboardDTO {

    private final int totalSubmitted;
    private final int totalReviewers;
    private final int totalEvaluated;
    private final int totalPending;
    private final Map<Paper, String> pendingByReviewer;

    public DashboardDTO(int totalSubmitted, int totalReviewers, int totalEvaluated,
                        int totalPending, Map<Paper, String> pendingByReviewer) {
        this.totalSubmitted = totalSubmitted;
        this.totalReviewers = totalReviewers;
        this.totalEvaluated = totalEvaluated;
        this.totalPending = totalPending;
        this.pendingByReviewer = pendingByReviewer;
    }

    public int getTotalSubmitted() { return totalSubmitted; }
    public int getTotalReviewers() { return totalReviewers; }
    public int getTotalEvaluated() { return totalEvaluated; }
    public int getTotalPending() { return totalPending; }
    public Map<Paper, String> getPendingByReviewer() { return pendingByReviewer; }
}