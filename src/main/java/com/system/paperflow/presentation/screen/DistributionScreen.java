package com.system.paperflow.presentation.screen;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.usecase.distribute.DistributePapersUseCase;
import com.system.paperflow.application.usecase.review.ListEventAssignmentsUseCase;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.ScreenUtils;
import com.system.paperflow.presentation.ui.component.Button;
import com.system.paperflow.presentation.ui.component.Card;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.Grid;
import com.system.paperflow.presentation.ui.component.Page;
import com.system.paperflow.presentation.ui.component.Row;
import com.system.paperflow.presentation.ui.component.StatusMessage;
import com.system.paperflow.presentation.ui.component.Text;

import javax.swing.JComponent;
import java.util.List;

public class DistributionScreen implements Screen {

    private final EventManager eventManager;
    private final DistributePapersUseCase distributePapersUseCase;
    private final ListEventAssignmentsUseCase listEventAssignmentsUseCase;

    public DistributionScreen(
            EventManager eventManager,
            DistributePapersUseCase distributePapersUseCase,
            ListEventAssignmentsUseCase listEventAssignmentsUseCase
    ) {
        this.eventManager = eventManager;
        this.distributePapersUseCase = distributePapersUseCase;
        this.listEventAssignmentsUseCase = listEventAssignmentsUseCase;
    }

    @Override
    public JComponent build() {
        Event event = eventManager.getCurrentEvent();
        StatusMessage status = StatusMessage.create();
        List<ReviewAssignment> assignments = listEventAssignmentsUseCase.execute(event);

        Column header = Column.create().gap(6);
        header.add(Text.title("Distribuição automática"));
        header.add(Text.subtitle("RF06 - Distribui artigos conforme compatibilidade temática e evita conflito de autoria."));

        Card actionCard = Card.create().gap(14);
        actionCard.add(Text.sectionTitle("Executar distribuição"));
        actionCard.add(Text.body("O sistema escolhe revisores do comitê considerando as áreas do artigo e as expertises cadastradas."));
        actionCard.add(Button.primary("Distribuir artigos").onClick(() -> {
            status.clear();
            try {
                List<ReviewAssignment> created = distributePapersUseCase.execute(event.getId());
                status.success(created.size() + " atribuição(ões) criada(s).");
                ScreenUtils.navigateTo("distribution");
            } catch (Exception exception) {
                status.error(exception.getMessage());
            }
        }));
        actionCard.add(status);

        Grid grid = Grid.columns(2);
        for (ReviewAssignment assignment : assignments) {
            Card card = Card.create().gap(8);
            card.add(Text.caption(assignment.isFinished() ? "Concluída" : "Pendente"));
            card.add(Text.sectionTitle(assignment.getPaper().getTitle()));
            card.add(Text.body("Revisor: " + assignment.getReviewer().getEmail()));
            card.add(Text.body("Status do artigo: " + assignment.getPaper().getStatus()));
            grid.add(card);
        }

        Card listCard = Card.create().gap(14);
        listCard.add(Text.sectionTitle("Artigos atribuídos"));
        listCard.add(assignments.isEmpty() ? Text.body("Nenhuma atribuição registrada ainda.") : grid);

        Row actions = Row.create().right().gap(12);
        actions.add(Button.secondary("Voltar ao evento").onClick(() -> ScreenUtils.navigateTo("event")));

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(actionCard);
        content.add(listCard);
        content.add(actions);

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Distribuição";
    }
}
