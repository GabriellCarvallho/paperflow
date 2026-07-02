package com.system.paperflow.presentation.screen;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.usecase.review.ListReviewerAssignmentsUseCase;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.presentation.context.ScreenContext;
import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.ScreenUtils;
import com.system.paperflow.presentation.ui.component.Button;
import com.system.paperflow.presentation.ui.component.Card;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.Grid;
import com.system.paperflow.presentation.ui.component.Page;
import com.system.paperflow.presentation.ui.component.Row;
import com.system.paperflow.presentation.ui.component.Text;

import javax.swing.JComponent;
import java.util.List;

public class ReviewerAssignmentsScreen implements Screen {

    private final ScreenContext context;
    private final EventManager eventManager;
    private final ListReviewerAssignmentsUseCase listReviewerAssignmentsUseCase;

    public ReviewerAssignmentsScreen(
            ScreenContext context,
            EventManager eventManager,
            ListReviewerAssignmentsUseCase listReviewerAssignmentsUseCase
    ) {
        this.context = context;
        this.eventManager = eventManager;
        this.listReviewerAssignmentsUseCase = listReviewerAssignmentsUseCase;
    }

    @Override
    public JComponent build() {
        Event event = eventManager.getCurrentEvent();
        List<ReviewAssignment> assignments = listReviewerAssignmentsUseCase.execute(event, context.currentUser());

        Column header = Column.create().gap(6);
        header.add(Text.title("Minhas revisões"));
        header.add(Text.subtitle("RF07 - Artigos atribuídos ao revisor logado."));

        Grid grid = Grid.columns(2);
        for (ReviewAssignment assignment : assignments) {
            grid.add(assignmentCard(assignment));
        }

        Row actions = Row.create().right().gap(12);
        actions.add(Button.secondary("Voltar ao evento").onClick(() -> ScreenUtils.navigateTo("event")));

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(assignments.isEmpty() ? Text.body("Nenhum artigo foi atribuído a você.") : grid);
        content.add(actions);

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Minhas Revisões";
    }

    private Card assignmentCard(ReviewAssignment assignment) {
        Card card = Card.create().gap(8);
        card.add(Text.caption(assignment.isFinished() ? "Revisão concluída" : "Revisão pendente"));
        card.add(Text.sectionTitle(assignment.getPaper().getTitle()));
        card.add(Text.body("Resumo: " + assignment.getPaper().getSummary()));
        card.add(Text.caption("Blind-review: os dados dos autores não são exibidos nesta tela."));

        if (assignment.isFinished()) {
            card.add(Text.body("Veredito: " + assignment.getReview().getVerdict()).bold());
        } else {
            card.add(Button.primary("Avaliar artigo").onClick(() -> {
                context.selectPaper(assignment.getPaper());
                ScreenUtils.navigateTo("submit-review");
            }));
        }

        return card;
    }
}
