package com.system.paperflow.application.filter;

import com.system.paperflow.domain.entity.Paper;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class PaperFilter {

    private PaperFilter next;

    public PaperFilter linkWith(PaperFilter next) {
        this.next = next;
        return next;
    }

    public List<Paper> apply(List<Paper> papers) {
        List<Paper> result = doFilter(papers);
        if (next != null) {
            return next.apply(result);
        }
        return result;
    }

    protected abstract List<Paper> doFilter(List<Paper> papers);

    protected List<Paper> filterBy(List<Paper> papers, Predicate<Paper> predicate) {
        return papers.stream().filter(predicate).collect(Collectors.toList());
    }
}
