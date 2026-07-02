package com.system.paperflow.presentation.screen;

import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.usecase.paper.ListAuthorPapersUseCase;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.domain.entity.ThematicArea;
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

public class MyPapersScreen implements Screen {

    private final ScreenContext context;
    private final ListAuthorPapersUseCase listAuthorPapersUseCase;
    private final ReviewAssignmentGateway assignmentGateway;

    public MyPapersScreen(ScreenContext context, ListAuthorPapersUseCase listAuthorPapersUseCase, ReviewAssignmentGateway assignmentGateway) {
        this.context = context;
        this.listAuthorPapersUseCase = listAuthorPapersUseCase;
        this.assignmentGateway = assignmentGateway;
    }

    @Override
    public JComponent build() {
        List<Paper> papers = listAuthorPapersUseCase.execute(context.currentUser().getEmail());

        Column header = Column.create().gap(6);
        header.add(Text.title("Meus artigos"));
        header.add(Text.subtitle("Acompanhe suas submissões e os pareceres quando disponíveis."));

        Grid grid = Grid.columns(2);
        for (Paper paper : papers) {
            grid.add(paperCard(paper));
        }

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(papers.isEmpty() ? Text.body("Nenhum artigo submetido por você.") : grid);

        Row actions = Row.create().right().gap(12);
        actions.add(Button.secondary("Voltar ao evento").onClick(() -> ScreenUtils.navigateTo("event")));
        actions.add(Button.primary("Nova submissão").onClick(() -> ScreenUtils.navigateTo("submit-paper")));
        content.add(actions);

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Meus Artigos";
    }

    private Card paperCard(Paper paper) {
        Card card = Card.create().gap(8);
        card.add(Text.caption("Status: " + paper.getStatus()));
        card.add(Text.sectionTitle(paper.getTitle()));
        card.add(Text.body("Áreas: " + paper.getAreas().stream().map(ThematicArea::name).toList()));
        card.add(Text.caption("ID: " + paper.getId()));

        List<ReviewAssignment> assignments = assignmentGateway.findByPaperId(paper.getId());
        for (ReviewAssignment assignment : assignments) {
            if (assignment.isFinished()) {
                card.add(Text.body("Parecer: " + assignment.getReview().getVerdict()).bold());
                card.add(Text.caption("Pontos positivos: " + assignment.getReview().getContribution()));
                card.add(Text.caption("Críticas: " + assignment.getReview().getCriticism()));
            }
        }

        return card;
    }
}
