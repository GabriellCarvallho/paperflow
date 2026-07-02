package com.system.paperflow.presentation.console.screen;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.usecase.event.CreateEventUseCase;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.EventCategory;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;

import java.time.LocalDate;

public class EventScreen extends BaseConsoleScreen {

    private final CreateEventUseCase createEventUseCase;

    public EventScreen(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            ConsoleRouter router,
            EventManager eventManager,
            ReviewAssignmentGateway assignmentGateway,
            CreateEventUseCase createEventUseCase
    ) {
        super(reader, printer, session, router, eventManager, assignmentGateway);
        this.createEventUseCase = createEventUseCase;
    }

    @Override
    public void show() {
        runSafely(this::createEvent);
        router.navigateTo(ConsoleRouter.COORDINATOR_MENU);
    }

    private void createEvent() {
        printer.section("CRIAR EVENTO");
        String name = reader.text("Nome do evento");
        String city = reader.text("Cidade");
        LocalDate endDate = reader.date("Data final do evento");
        LocalDate submissionDeadline = reader.date("Prazo final de submissao");
        EventCategory category = chooseCategory();

        Event event = createEventUseCase.execute(name, city, endDate, submissionDeadline, category);
        printer.success("Evento criado: " + event.getName());
        printer.info("ID: " + event.getId());
    }
}
