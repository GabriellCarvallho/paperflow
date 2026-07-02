package com.system.paperflow.application.dto;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.ReviewAssignment;

import java.util.List;

public record AuthorNotificationData(
        Paper paper,
        List<ReviewAssignment> assignments
) {
}
