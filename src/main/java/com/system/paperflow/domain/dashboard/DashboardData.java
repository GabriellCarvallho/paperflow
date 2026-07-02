package com.system.paperflow.domain.dashboard;

public record DashboardData(
        int totalSubmitted,
        int totalReviewers,
        int totalEvaluated,
        int totalPending
) {}
