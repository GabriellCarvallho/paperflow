package com.system.paperflow.presentation.console.screen;

import com.system.paperflow.application.command.CommandExecutor;
import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.usecase.committee.AddReviewerUseCase;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;

import java.util.List;

public class CommitteeScreen extends BaseConsoleScreen {

    private final AddReviewerUseCase addReviewerUseCase;

    public CommitteeScreen(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            ConsoleRouter router,
            EventManager eventManager,
            ReviewAssignmentGateway assignmentGateway,
            CommandExecutor commandExecutor,
            AddReviewerUseCase addReviewerUseCase
    ) {
        super(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor);
        this.addReviewerUseCase = addReviewerUseCase;
    }

    @Override
    public void show() {
        runSafely(this::addReviewer);
        router.navigateTo(ConsoleRouter.COORDINATOR_MENU);
    }

    private void addReviewer() {
        Event event = currentEvent();
        printer.section("ADICIONAR REVISOR");
        printThematicAreas(event);
        String email = reader.text("Email do pesquisador cadastrado");
        List<String> areas = reader.csv("Areas de expertise separadas por virgula");
        Researcher reviewer = executeCommand(
                () -> addReviewerUseCase.execute(session.currentUser(), email, areas),
                session.currentUser().getEmail() + " ADICIONOU revisor " + email + " ao evento " + event.getId()
        );
        printer.success("Revisor adicionado ao comite: " + reviewer.getEmail());
    }
}
