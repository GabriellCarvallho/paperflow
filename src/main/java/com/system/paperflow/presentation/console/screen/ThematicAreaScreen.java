package com.system.paperflow.presentation.console.screen;

import com.system.paperflow.application.command.CommandExecutor;
import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.usecase.thematic.CreateThematicAreaUseCase;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.ThematicArea;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;

public class ThematicAreaScreen extends BaseConsoleScreen {

    private final CreateThematicAreaUseCase createThematicAreaUseCase;

    public ThematicAreaScreen(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            ConsoleRouter router,
            EventManager eventManager,
            ReviewAssignmentGateway assignmentGateway,
            CommandExecutor commandExecutor,
            CreateThematicAreaUseCase createThematicAreaUseCase
    ) {
        super(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor);
        this.createThematicAreaUseCase = createThematicAreaUseCase;
    }

    @Override
    public void show() {
        runSafely(this::createThematicArea);
        router.navigateTo(ConsoleRouter.COORDINATOR_MENU);
    }

    private void createThematicArea() {
        Event event = currentEvent();
        printer.section("CADASTRAR AREA TEMATICA");
        printThematicAreas(event);
        String areaName = reader.text("Nova area");
        ThematicArea area = executeCommand(
                () -> createThematicAreaUseCase.execute(session.currentUser(), event.getId(), areaName),
                session.currentUser().getEmail() + " CADASTROU area tematica \"" + areaName + "\" no evento " + event.getId()
        );
        printer.success("Area cadastrada: " + area.name());
    }
}
