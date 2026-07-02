package com.system.paperflow.presentation.console.screen;

import com.system.paperflow.application.command.CommandExecutor;
import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.usecase.paper.ListEventPapersUseCase;
import com.system.paperflow.application.usecase.review.ListEventAssignmentsUseCase;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.PaperStatus;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;

import java.util.List;

public class DashboardScreen extends BaseConsoleScreen {

    private final ListEventPapersUseCase listEventPapersUseCase;
    private final ListEventAssignmentsUseCase listEventAssignmentsUseCase;

    public DashboardScreen(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            ConsoleRouter router,
            EventManager eventManager,
            ReviewAssignmentGateway assignmentGateway,
            CommandExecutor commandExecutor,
            ListEventPapersUseCase listEventPapersUseCase,
            ListEventAssignmentsUseCase listEventAssignmentsUseCase
    ) {
        super(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor);
        this.listEventPapersUseCase = listEventPapersUseCase;
        this.listEventAssignmentsUseCase = listEventAssignmentsUseCase;
    }

    @Override
    public void show() {
        runSafely(this::dashboard);
        router.navigateTo(ConsoleRouter.COORDINATOR_MENU);
    }

    private void dashboard() {
        Event event = currentEvent();
        printer.section("DASHBOARD");
        List<Paper> papers = executeCommand(
                () -> listEventPapersUseCase.execute(event),
                session.currentUser().getEmail() + " CONSULTOU dashboard do evento " + event.getId()
        );
        List<ReviewAssignment> assignments = listEventAssignmentsUseCase.execute(event);
        long evaluatedPapers = papers.stream()
                .filter(paper -> paper.getStatus() == PaperStatus.ACCEPTED || paper.getStatus() == PaperStatus.REJECTED)
                .count();
        long pendingAssignments = assignments.stream().filter(assignment -> !assignment.isFinished()).count();

        printer.table(
                List.of("Indicador", "Valor"),
                List.of(
                        List.of("Artigos submetidos", String.valueOf(papers.size())),
                        List.of("Revisores no comite", String.valueOf(event.getCommittee().size())),
                        List.of("Artigos avaliados", String.valueOf(evaluatedPapers)),
                        List.of("Revisoes pendentes", String.valueOf(pendingAssignments))
                )
        );

        List<ReviewAssignment> pending = assignments.stream()
                .filter(assignment -> !assignment.isFinished())
                .toList();
        if (!pending.isEmpty()) {
            printer.section("PENDENCIAS");
            printAssignments(pending, false);
        }
    }
}
