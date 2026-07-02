package com.system.paperflow.presentation.screen;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.presentation.context.ScreenContext;
import com.system.paperflow.presentation.ui.Screen;
import com.system.paperflow.presentation.ui.ScreenUtils;
import com.system.paperflow.presentation.ui.View;
import com.system.paperflow.presentation.ui.component.Button;
import com.system.paperflow.presentation.ui.component.Card;
import com.system.paperflow.presentation.ui.component.Center;
import com.system.paperflow.presentation.ui.component.ClickableCard;
import com.system.paperflow.presentation.ui.component.Column;
import com.system.paperflow.presentation.ui.component.Grid;
import com.system.paperflow.presentation.ui.component.Page;
import com.system.paperflow.presentation.ui.component.Row;
import com.system.paperflow.presentation.ui.component.Text;

import javax.swing.JComponent;
import java.time.format.DateTimeFormatter;

public class EventsScreen implements Screen {

    private final ScreenContext context;
    private final EventManager eventManager;

    public EventsScreen(ScreenContext context, EventManager eventManager) {
        this.context = context;
        this.eventManager = eventManager;
    }

    @Override
    public JComponent build() {
        Researcher researcher = context.currentUser();

        Column greeting = Column.create().gap(6);
        greeting.add(Text.title("Olá, " + researcher.getUsername()));
        greeting.add(Text.subtitle("Acesse o evento atual ou crie um novo ciclo de submissões."));

        Row header = Row.create().spaceBetween();
        header.add(greeting);

        Row actions = Row.create().gap(12);
        if (researcher.isCoordinator()) {
            actions.add(Button.primary("Criar novo evento").onClick(() -> ScreenUtils.navigateTo("event-create")));
        }
        actions.add(Button.secondary("Sair").onClick(() -> {
            context.endSession();
            ScreenUtils.navigateTo("login");
        }));
        header.add(actions);

        Column content = Column.create().gap(24);
        content.add(header);

        if (eventManager.hasEvent()) {
            Grid grid = Grid.columns(3);
            grid.add(eventCard(eventManager.getCurrentEvent()));
            content.add(grid);
        } else {
            String message = researcher.isCoordinator()
                    ? "Nenhum evento foi criado ainda. Clique em 'Criar novo evento' para começar."
                    : "Nenhum evento está disponível no momento.";
            content.add(Center.create(Text.sectionTitle(message)).withMinHeight(420));
        }

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Eventos";
    }

    private View eventCard(Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String period = event.getStartDate().format(formatter) + " a " + event.getEndDate().format(formatter);
        String status = event.isStarted() ? "Evento iniciado" : "Em preparação";
        String deadline = "Submissões até " + event.getSubmissionDeadline().format(formatter);

        ClickableCard card = ClickableCard.create()
                .onClick(() -> ScreenUtils.navigateTo("event"))
                .withMinHeight(240)
                .gap(10);

        card.add(Text.caption(status));
        card.add(Text.sectionTitle(event.getName()));
        card.add(Text.body(event.getCity()));
        card.add(Text.caption(period));
        card.add(Text.caption(deadline));
        card.add(Text.body("Categoria: " + event.getCategory()).bold());

        return card;
    }
}
