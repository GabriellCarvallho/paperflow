package com.system.paperflow.application.filter;

import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Topic;
import java.util.List;

public class TopicFilter extends PaperFilter {

    private final Topic topic;

    public TopicFilter(Topic topic) {
        this.topic = topic;
    }

    @Override
    protected List<Paper> doFilter(List<Paper> papers) {
        return filterBy(papers, p -> p.getTopics().contains(topic));
    }
}
