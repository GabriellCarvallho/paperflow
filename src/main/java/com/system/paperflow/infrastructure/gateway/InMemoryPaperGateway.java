package com.system.paperflow.infrastructure.gateway;

import com.system.paperflow.application.gateway.PaperGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryPaperGateway implements PaperGateway {

    private final List<Paper> papers = new ArrayList<>();

    @Override
    public Paper save(Paper paper) {
        papers.removeIf(existing -> existing.getId().equals(paper.getId()));
        papers.add(paper);
        return paper;
    }

    @Override
    public Optional<Paper> findById(String id) {
        return papers.stream()
                .filter(paper -> paper.getId().toString().equals(id))
                .findFirst();
    }

    @Override
    public List<Paper> findByEvent(Event event) {
        return papers.stream()
                .filter(paper -> paper.getEvent().equals(event))
                .toList();
    }

    @Override
    public List<Paper> findByAuthorEmail(String authorEmail) {
        String normalizedEmail = normalize(authorEmail);

        return papers.stream()
                .filter(paper -> paper.getAuthor().getEmail().equalsIgnoreCase(normalizedEmail))
                .toList();
    }

    private String normalize(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
