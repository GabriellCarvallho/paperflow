//package com.system.paperflow.presentation.screen;
//
//import javax.swing.JComponent;
//
//import com.system.paperflow.application.usecase.dashboard.DashboardUseCase;
//import com.system.paperflow.application.usecase.paper.ListEventPapersUseCase;
//import com.system.paperflow.domain.dashboard.DashboardData;
//import com.system.paperflow.domain.entity.Paper;
//import com.system.paperflow.domain.entity.ReviewAssignment;
//import com.system.paperflow.presentation.context.ScreenContext;
//import com.system.paperflow.presentation.ui.Screen;
//import com.system.paperflow.presentation.ui.ScreenUtils;
//import com.system.paperflow.presentation.ui.View;
//import com.system.paperflow.presentation.ui.component.Button;
//import com.system.paperflow.presentation.ui.component.Card;
//import com.system.paperflow.presentation.ui.component.Column;
//import com.system.paperflow.presentation.ui.component.Grid;
//import com.system.paperflow.presentation.ui.component.Page;
//import com.system.paperflow.presentation.ui.component.Text;
//
//public class DashboardScreen implements Screen {
//
//    private final ScreenContext context;
//    private final DashboardUseCase dashboardUseCase;
//    private final ListEventPapersUseCase listEventPapersUseCase;
//    private final ListTechnicalCommitteeUseCase listTechnicalCommitteeUseCase;
//    private final ListEventAssignmentsUseCase listEventAssignmentsUseCase;
//
//    public DashboardScreen(
//            ScreenContext context,
//            DashboardUseCase dashboardUseCase,
//            ListEventPapersUseCase listEventPapersUseCase,
//            ListTechnicalCommitteeUseCase listTechnicalCommitteeUseCase,
//            ListEventAssignmentsUseCase listEventAssignmentsUseCase
//    ) {
//        this.context = context;
//        this.dashboardUseCase = dashboardUseCase;
//        this.listEventPapersUseCase = listEventPapersUseCase;
//        this.listTechnicalCommitteeUseCase = listTechnicalCommitteeUseCase;
//        this.listEventAssignmentsUseCase = listEventAssignmentsUseCase;
//    }
//
//    @Override
//    public JComponent build() {
//        DashboardData data = dashboardUseCase.execute(listEventPapersUseCase.execute(context.currentEvent()), listTechnicalCommitteeUseCase.execute());
//
//        Column header = Column.create().gap(6);
//        header.add(Text.title("Dashboard"));
//        header.add(Text.subtitle("Resumo do evento " + context.currentEvent().getName()));
//
//        Grid metrics = Grid.columns(4);
//        metrics.add(metricCard("Artigos submetidos", String.valueOf(data.totalSubmitted()), "Total recebido"));
//        metrics.add(metricCard("Revisores ativos", String.valueOf(data.totalReviewers()), "Comitê aceito"));
//        metrics.add(metricCard("Avaliados", String.valueOf(data.totalEvaluated()), "Com parecer final"));
//        metrics.add(metricCard("Pendentes", String.valueOf(data.totalPending()), "Ainda sem resultado"));
//
//        Grid cards = Grid.columns(2);
//        cards.add(reviewCard());
//        cards.add(pendingAssignmentsCard());
//
//        Column content = Column.create().gap(24);
//        content.add(header);
//        content.add(metrics);
//        content.add(cards);
//        content.add(Button.secondary("Voltar ao evento").onClick(() -> ScreenUtils.navigateTo("event")));
//
//        return Page.create().add(content).build();
//    }
//
//    @Override
//    public String withTitle() {
//        return "Paper Flow - Dashboard";
//    }
//
//    private View metricCard(String title, String value, String caption) {
//        Card card = Card.create().withMinHeight(132).gap(8);
//        card.add(Text.caption(title));
//        card.add(Text.metric(value));
//        card.add(Text.caption(caption));
//        return card;
//    }
//
//    private View reviewCard() {
//        Card card = Card.create().withMinHeight(230).gap(14);
//        card.add(Text.sectionTitle("Fila de revisão"));
//
//        for (Paper paper : listEventPapersUseCase.execute(context.currentEvent())) {
//            card.add(activity(paper.getTitle(), paper.getStatus()));
//        }
//
//        if (listEventPapersUseCase.execute(context.currentEvent()).isEmpty()) {
//            card.add(Text.body("Nenhum artigo submetido."));
//        }
//
//        return card;
//    }
//
//    private View pendingAssignmentsCard() {
//        Card card = Card.create().withMinHeight(230).gap(14);
//        card.add(Text.sectionTitle("Pendências por revisor"));
//
//        for (ReviewAssignment assignment : listEventAssignmentsUseCase.execute(context.currentEvent())) {
//            if (!assignment.isFinished()) {
//                card.add(activity(assignment.getPaper().getTitle(), assignment.getReviewer().getEmail()));
//            }
//        }
//
//        if (listEventAssignmentsUseCase.execute(context.currentEvent()).stream().noneMatch(assignment -> !assignment.isFinished())) {
//            card.add(Text.body("Nenhuma revisão pendente."));
//        }
//
//        return card;
//    }
//
//    private View activity(String title, String caption) {
//        Column column = Column.create().gap(4);
//        column.add(Text.body(title).bold());
//        column.add(Text.caption(caption));
//        return column;
//    }
//}
