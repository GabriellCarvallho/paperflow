package com.system.paperflow.presentation.screen;

import javax.swing.JComponent;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.usecase.paper.ListEventPapersUseCase;
import com.system.paperflow.application.usecase.review.ListEventAssignmentsUseCase;
import com.system.paperflow.domain.dashboard.DashboardData;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.ScreenUtils;
import com.system.paperflow.presentation.ui.View;
import com.system.paperflow.presentation.ui.component.Button;
import com.system.paperflow.presentation.ui.component.Card;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.Grid;
import com.system.paperflow.presentation.ui.component.Page;
import com.system.paperflow.presentation.ui.component.Text;

import java.util.List;

public class DashboardScreen implements Screen {

    private final EventManager eventManager;
    private final ListEventPapersUseCase listEventPapersUseCase;
    private final ListEventAssignmentsUseCase listEventAssignmentsUseCase;

    public DashboardScreen(
            EventManager eventManager,
            ListEventPapersUseCase listEventPapersUseCase,
            ListEventAssignmentsUseCase listEventAssignmentsUseCase
    ) {
        this.eventManager = eventManager;
        this.listEventPapersUseCase = listEventPapersUseCase;
        this.listEventAssignmentsUseCase = listEventAssignmentsUseCase;
    }

    @Override
    public JComponent build() {
        Event event = eventManager.getCurrentEvent();
        List<Paper> papers = listEventPapersUseCase.execute(event);
        List<ReviewAssignment> assignments = listEventAssignmentsUseCase.execute(event);

        DashboardData data = new DashboardData(
                papers.size(),
                event.getCommittee().size(),
                (int) assignments.stream().filter(ReviewAssignment::isFinished).count(),
                (int) assignments.stream().filter(assignment -> !assignment.isFinished()).count()
        );

        Column header = Column.create().gap(6);
        header.add(Text.title("Dashboard"));
        header.add(Text.subtitle("RF08 - Resumo do evento " + event.getName()));

        Grid metrics = Grid.columns(4);
        metrics.add(metricCard("Artigos submetidos", String.valueOf(data.totalSubmitted()), "Total recebido"));
        metrics.add(metricCard("Revisores", String.valueOf(data.totalReviewers()), "Comitê técnico"));
        metrics.add(metricCard("Avaliados", String.valueOf(data.totalEvaluated()), "Com parecer"));
        metrics.add(metricCard("Pendentes", String.valueOf(data.totalPending()), "Ainda sem parecer"));

        Grid cards = Grid.columns(2);
        cards.add(reviewCard(papers));
        cards.add(pendingAssignmentsCard(assignments));

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(metrics);
        content.add(cards);
        content.add(Button.secondary("Voltar ao evento").onClick(() -> ScreenUtils.navigateTo("event")));

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Dashboard";
    }

    private View metricCard(String title, String value, String caption) {
        Card card = Card.create().withMinHeight(132).gap(8);
        card.add(Text.caption(title));
        card.add(Text.metric(value));
        card.add(Text.caption(caption));
        return card;
    }

    private View reviewCard(List<Paper> papers) {
        Card card = Card.create().withMinHeight(230).gap(14);
        card.add(Text.sectionTitle("Artigos do evento"));

        for (Paper paper : papers) {
            card.add(activity(paper.getTitle(), String.valueOf(paper.getStatus())));
        }

        if (papers.isEmpty()) {
            card.add(Text.body("Nenhum artigo submetido."));
        }

        return card;
    }

    private View pendingAssignmentsCard(List<ReviewAssignment> assignments) {
        Card card = Card.create().withMinHeight(230).gap(14);
        card.add(Text.sectionTitle("Pendências por revisor"));

        boolean hasPending = false;
        for (ReviewAssignment assignment : assignments) {
            if (!assignment.isFinished()) {
                hasPending = true;
                card.add(activity(assignment.getPaper().getTitle(), assignment.getReviewer().getEmail()));
            }
        }

        if (!hasPending) {
            card.add(Text.body("Nenhuma revisão pendente."));
        }

        return card;
    }

    private View activity(String title, String caption) {
        Column column = Column.create().gap(4);
        column.add(Text.body(title).bold());
        column.add(Text.caption(caption));
        return column;
    }
}
