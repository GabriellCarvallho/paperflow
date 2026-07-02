package com.system.paperflow.presentation.console;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.system.paperflow.application.factory.ResearcherCreator;
import com.system.paperflow.application.filter.PaperFilter;
import com.system.paperflow.application.filter.StatusFilter;
import com.system.paperflow.application.filter.TopicFilter;
import com.system.paperflow.application.persistence.EventPersistence;
import com.system.paperflow.application.persistence.PaperPersistence;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.application.usecase.dashboard.DashboardUseCase;
import com.system.paperflow.application.usecase.event.ListEventsUseCase;
import com.system.paperflow.application.usecase.event.StartEventUseCase;
import com.system.paperflow.application.usecase.filter.FilterPapersUseCase;
import com.system.paperflow.domain.dashboard.DashboardData;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.Topic;
import com.system.paperflow.domain.entity.User;
import com.system.paperflow.domain.state.UnderReviewState;
import com.system.paperflow.infrastructure.sqlite.SQLiteEvent;
import com.system.paperflow.infrastructure.sqlite.SQLitePaper;
import com.system.paperflow.infrastructure.sqlite.SQLiteUser;

public class GabrielSubmissionDemo {

    public static void main(String[] args) {

        String dbPath = "data/paper-flow-demo.db";
        UserPersistence userPersistence = new SQLiteUser(dbPath);
        EventPersistence eventPersistence = new SQLiteEvent(dbPath);
        PaperPersistence paperPersistence = new SQLitePaper(dbPath, userPersistence);

        // ===== RF01 - Start do evento (Use Case + SQLite) =====
        StartEventUseCase startEvent = new StartEventUseCase(eventPersistence);
        Event event = startEvent.execute(
                "SBSI 2026", "Vitoria - ES",
                LocalDate.of(2026, 5, 25),
                LocalDate.of(2026, 5, 28),
                LocalDate.of(2026, 12, 31));

        System.out.println("Event started: " + event.getName());
        System.out.println("Open for submissions: " + event.isOpenForSubmissions());
        System.out.println("Finished: " + event.isFinished());
        System.out.println();

        ListEventsUseCase listEvents = new ListEventsUseCase(eventPersistence);
        System.out.println("=== All events in database ===");
        for (Event e : listEvents.execute()) {
            System.out.println("- " + e.getName() + " (" + e.getCity() + ")");
        }
        System.out.println();

        // ===== RF05 - Submissao (State) =====
        String authorEmail = "ana" + System.currentTimeMillis() + "@ufpb.br";
        User authorUser = new ResearcherCreator().create("Ana", authorEmail, "1234", "UFPB");
        userPersistence.save(authorUser);
        Researcher author = (Researcher) authorUser;

        Topic aiTopic = new Topic("IA", "Inteligencia Artificial");

        Paper paper = new Paper("P" + System.currentTimeMillis(), "Design Patterns in Health Systems",
                "A study on design patterns.", author,
                new ArrayList<>(), Set.of(aiTopic), event);

        System.out.println("Paper submitted: " + paper.getTitle());
        System.out.println("Status: " + paper.getStatus());

        paper.advanceState();
        System.out.println("Status: " + paper.getStatus());

        UnderReviewState reviewState = (UnderReviewState) paper.getState();
        reviewState.accept(paper);
        System.out.println("Status: " + paper.getStatus());

        paperPersistence.save(paper);
        System.out.println("Paper saved to database.");
        System.out.println();

        Paper paper2 = new Paper("P" + (System.currentTimeMillis() + 1), "Another AI paper",
                "summary", author, new ArrayList<>(), Set.of(aiTopic), event);
        paperPersistence.save(paper2);

        // ===== RF08 - Dashboard (Use Case + SQLite) =====
        DashboardUseCase dashboardUseCase = new DashboardUseCase(paperPersistence);
        DashboardData data = dashboardUseCase.execute();

        System.out.println("=== Dashboard (from database) ===");
        System.out.println("Submitted: " + data.totalSubmitted());
        System.out.println("Evaluated: " + data.totalEvaluated());
        System.out.println("Pending: " + data.totalPending());
        System.out.println();

        // ===== RF10 - Filtro de artigos (Chain of Responsibility) =====
        List<Paper> allPapers = paperPersistence.findAll();

        PaperFilter chain = new StatusFilter("Submitted");

        FilterPapersUseCase filterUseCase = new FilterPapersUseCase();
        List<Paper> filtered = filterUseCase.execute(allPapers, chain);

        System.out.println("=== Filter (Status=Submitted) ===");
        for (Paper p : filtered) {
            System.out.println("- " + p.getTitle() + " [" + p.getStatus() + "]");
        }
    }
}
