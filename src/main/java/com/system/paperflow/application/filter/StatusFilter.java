package com.system.paperflow.application.filter;

import com.system.paperflow.domain.entity.Paper;
import java.util.List;

public class StatusFilter extends PaperFilter {

    private final String status;

    public StatusFilter(String status) {
        this.status = status;
    }

    @Override
    protected List<Paper> doFilter(List<Paper> papers) {
        return filterBy(papers, p -> p.getStatus().equals(status));
    }
}
