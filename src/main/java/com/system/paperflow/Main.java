package com.system.paperflow;

import com.system.paperflow.application.builder.EventBuilder;
import com.system.paperflow.application.service.EventManager;
import com.system.paperflow.application.dashboard.DashboardService;
import com.system.paperflow.application.dashboard.DashboardDTO;
import com.system.paperflow.application.dashboard.SystemEventPublisher;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.Topic;
import com.system.paperflow.domain.state.UnderReviewState;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        // ===== RF01 - Start do evento (Builder) =====
        EventManager eventManager = new EventManager();

        Event event = new EventBuilder()
                .withName("SBSI 2026")
                .withCity("Vitória - ES")
                .withPeriod(LocalDate.of(2026, 5, 25), LocalDate.of(2026, 5, 28))
                .withSubmissionDeadline(LocalDate.of(2026, 12, 31))
                .build();

        eventManager.startNewEvent(event);
        System.out.println("Event started: " + eventManager.getCurrentEvent().getName());
        System.out.println("Open for submissions: " + event.isOpenForSubmissions());
        System.out.println();

        // ===== RF05 - Submissão e ciclo de vida (State) =====
        Researcher author = new Researcher("Ana", "ana@ufpb.br", "1234", "UFPB");

        Paper paper = new Paper(
                "P001",
                "Design Patterns aplicados em sistemas de saúde",
                "Um estudo sobre padrões de projeto.",
                author,
                new ArrayList<>(),
                Set.of(new Topic("IA", "Inteligência Artificial")),
                event
        );

        System.out.println("Paper submitted: " + paper.getTitle());
        System.out.println("Status: " + paper.getStatus());   // Submitted

        paper.advanceState();                                  // → Under Review
        System.out.println("Status: " + paper.getStatus());

        UnderReviewState reviewState = (UnderReviewState) paper.getState();
        reviewState.accept(paper);                             // → Accepted
        System.out.println("Status: " + paper.getStatus());
        System.out.println();

        // ===== RF08 - Dashboard (Observer) =====
        List<Paper> papers = List.of(paper);
        List<Researcher> reviewers = List.of(author);

        DashboardService dashboard = new DashboardService(papers, reviewers);

        SystemEventPublisher publisher = new SystemEventPublisher();
        publisher.subscribe(dashboard);   // dashboard observa o sistema
        publisher.notifyObservers();      // dispara atualização

        DashboardDTO data = dashboard.generateDashboard();
        System.out.println("=== Dashboard ===");
        System.out.println("Submitted: " + data.getTotalSubmitted());
        System.out.println("Reviewers: " + data.getTotalReviewers());
        System.out.println("Evaluated: " + data.getTotalEvaluated());
        System.out.println("Pending: " + data.getTotalPending());
    }
}