package com.system.paperflow.application.usecase.filter;

import com.system.paperflow.application.filter.PaperFilter;
import com.system.paperflow.domain.entity.Paper;
import java.util.List;

public class FilterPapersUseCase {

    public List<Paper> execute(List<Paper> papers, PaperFilter filterChain) {
        return filterChain.apply(papers);
    }
}
