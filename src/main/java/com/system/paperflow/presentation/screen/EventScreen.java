package com.system.paperflow.presentation.screen;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.usecase.event.StartEventUseCase;
import com.system.paperflow.application.usecase.paper.ListEventPapersUseCase;
import com.system.paperflow.application.usecase.review.ListEventAssignmentsUseCase;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.presentation.context.ScreenContext;
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
import java.time.format.DateTimeFormatter;

public class EventScreen implements Screen {

    private final ScreenContext context;
    private final EventManager eventManager;
    private final StartEventUseCase startEventUseCase;
    private final ListEventPapersUseCase listEventPapersUseCase;
    private final ListEventAssignmentsUseCase listEventAssignmentsUseCase;

    public EventScreen(
            ScreenContext context,
            EventManager eventManager,
            StartEventUseCase startEventUseCase,
            ListEventPapersUseCase listEventPapersUseCase,
            ListEventAssignmentsUseCase listEventAssignmentsUseCase
    ) {
        this.context = context;
        this.eventManager = eventManager;
        this.startEventUseCase = startEventUseCase;
        this.listEventPapersUseCase = listEventPapersUseCase;
        this.listEventAssignmentsUseCase = listEventAssignmentsUseCase;
    }

    @Override
    public JComponent build() {
        Researcher researcher = context.currentUser();
        Event event = eventManager.getCurrentEvent();
        StatusMessage status = StatusMessage.create();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Row header = Row.create().spaceBetween();
        Column title = Column.create().gap(6);
        title.add(Text.title(event.getName()));
        title.add(Text.subtitle("Painel principal do evento."));
        header.add(title);
        header.add(Button.secondary("Voltar").onClick(() -> ScreenUtils.navigateTo("events")));

        Grid metrics = Grid.columns(4);
        metrics.add(metric("Status", event.isStarted() ? "Iniciado" : "Preparação"));
        metrics.add(metric("Submissões", String.valueOf(listEventPapersUseCase.execute(event).size())));
        metrics.add(metric("Revisões", String.valueOf(listEventAssignmentsUseCase.execute(event).size())));
        metrics.add(metric("Prazo", event.getSubmissionDeadline().format(formatter)));

        Card info = Card.create().gap(10);
        info.add(Text.sectionTitle("Informações do evento"));
        info.add(Text.body("Local: " + event.getCity()));
        info.add(Text.body("Período: " + event.getStartDate().format(formatter) + " a " + event.getEndDate().format(formatter)));
        info.add(Text.body("Categoria: " + event.getCategory()));
        info.add(Text.body("Áreas cadastradas: " + event.getThematicAreas().size()));
        info.add(Text.body("Revisores no comitê: " + event.getCommittee().size()));

        Card actions = Card.create().gap(12);
        actions.add(Text.sectionTitle("Ações disponíveis"));

        if (researcher.isCoordinator()) {
            actions.add(Text.body("Coordenação: configure áreas, comitê, distribuição e notificações."));
            actions.add(Button.primary("RF03 - Áreas temáticas").onClick(() -> ScreenUtils.navigateTo("topics")));
            actions.add(Button.primary("RF04 - Comitê técnico").onClick(() -> ScreenUtils.navigateTo("committee")));
            actions.add(Button.primary("RF06 - Distribuir artigos").onClick(() -> ScreenUtils.navigateTo("distribution")));
            actions.add(Button.primary("RF08 - Dashboard").onClick(() -> ScreenUtils.navigateTo("dashboard")));
            actions.add(Button.primary("RF09 - Notificar autores").onClick(() -> ScreenUtils.navigateTo("notifications")));
            actions.add(Button.secondary("RF10 - Requisito adicional").onClick(() -> ScreenUtils.navigateTo("rf10")));

            if (!event.isStarted()) {
                actions.add(Button.secondary("Abrir submissões").onClick(() -> {
                    try {
                        startEventUseCase.execute(event.getId());
                        ScreenUtils.navigateTo("event");
                    } catch (Exception exception) {
                        status.error(exception.getMessage());
                    }
                }));
            }
        } else {
            actions.add(Text.body("Participante: submeta artigos ou conclua avaliações recebidas."));
            actions.add(Button.primary("RF05 - Submeter artigo").onClick(() -> ScreenUtils.navigateTo("submit-paper")));
            actions.add(Button.primary("Meus artigos").onClick(() -> ScreenUtils.navigateTo("my-papers")));
            actions.add(Button.primary("RF07 - Minhas revisões").onClick(() -> ScreenUtils.navigateTo("reviewer-assignments")));
        }
        actions.add(status);

        Column content = Column.create().gap(24);
        content.add(header);
        content.add(metrics);
        content.add(info);
        content.add(actions);

        return Page.create().add(content).build();
    }

    @Override
    public String withTitle() {
        return "Paper Flow - Evento";
    }

    private Card metric(String label, String value) {
        Card card = Card.create().gap(6);
        card.add(Text.caption(label));
        card.add(Text.metric(value));
        return card;
    }
}
