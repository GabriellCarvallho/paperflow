package com.system.paperflow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.system.paperflow.application.filter.PaperFilter;
import com.system.paperflow.application.filter.StatusFilter;
import com.system.paperflow.application.filter.TopicFilter;
import com.system.paperflow.application.usecase.dashboard.DashboardUseCase;
import com.system.paperflow.application.usecase.event.StartEventUseCase;
import com.system.paperflow.application.usecase.filter.FilterPapersUseCase;
import com.system.paperflow.domain.dashboard.DashboardData;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.Topic;
import com.system.paperflow.domain.state.UnderReviewState;

public class Main {

    public static void main(String[] args) {

        // ===== RF01 - Start do evento (Use Case) =====
        StartEventUseCase startEvent = new StartEventUseCase();
        Event event = startEvent.execute(
                "SBSI 2026", "Vitoria - ES",
                LocalDate.of(2026, 5, 25),
                LocalDate.of(2026, 5, 28),
                LocalDate.of(2026, 12, 31));

        System.out.println("Event started: " + event.getName());
        System.out.println("Open for submissions: " + event.isOpenForSubmissions());
        System.out.println();

        // ===== RF05 - Submissao (State) =====
        Researcher author = new Researcher("Ana", "ana@ufpb.br", "1234", "UFPB");
        Topic aiTopic = new Topic("IA", "Inteligencia Artificial");

        Paper paper = new Paper("P001", "Design Patterns in Health Systems",
                "A study on design patterns.", author,
                new ArrayList<>(), Set.of(aiTopic), event);

        System.out.println("Paper submitted: " + paper.getTitle());
        System.out.println("Status: " + paper.getStatus());

        paper.advanceState();
        System.out.println("Status: " + paper.getStatus());

        UnderReviewState reviewState = (UnderReviewState) paper.getState();
        reviewState.accept(paper);
        System.out.println("Status: " + paper.getStatus());
        System.out.println();

        // ===== RF08 - Dashboard (Use Case + record) =====
        Paper paper2 = new Paper("P002", "Another AI paper",
                "summary", author, new ArrayList<>(), Set.of(aiTopic), event);

        List<Paper> papers = List.of(paper, paper2);
        List<Researcher> reviewers = List.of(author);

        DashboardUseCase dashboardUseCase = new DashboardUseCase();
        DashboardData data = dashboardUseCase.execute(papers, reviewers);

        System.out.println("=== Dashboard ===");
        System.out.println("Submitted: " + data.totalSubmitted());
        System.out.println("Reviewers: " + data.totalReviewers());
        System.out.println("Evaluated: " + data.totalEvaluated());
        System.out.println("Pending: " + data.totalPending());
        System.out.println();

        // ===== RF10 - Filtro de artigos (Chain of Responsibility) =====
        PaperFilter chain = new TopicFilter(aiTopic);
        chain.linkWith(new StatusFilter("Submitted"));

        FilterPapersUseCase filterUseCase = new FilterPapersUseCase();
        List<Paper> filtered = filterUseCase.execute(papers, chain);

        System.out.println("=== Filter (Topic=IA + Status=Submitted) ===");
        for (Paper p : filtered) {
            System.out.println("- " + p.getTitle() + " [" + p.getStatus() + "]");
        }
    }
}
