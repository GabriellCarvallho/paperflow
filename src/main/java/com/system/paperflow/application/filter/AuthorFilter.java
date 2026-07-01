package com.system.paperflow.application.filter;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import java.util.List;

public class AuthorFilter extends PaperFilter {

    private final Researcher author;

    public AuthorFilter(Researcher author) {
        this.author = author;
    }

    @Override
    protected List<Paper> doFilter(List<Paper> papers) {
        return filterBy(papers, p -> p.getAuthor().equals(author));
    }
}
