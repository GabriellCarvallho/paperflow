package com.system.paperflow.presentation.console.screen;

import com.system.paperflow.application.command.CommandExecutor;
import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.usecase.review.ListReviewerAssignmentsUseCase;
import com.system.paperflow.application.usecase.review.SubmitReviewUseCase;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.domain.entity.ReviewVerdict;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;

import java.util.List;

public class ReviewScreen extends BaseConsoleScreen {

    private final ListReviewerAssignmentsUseCase listReviewerAssignmentsUseCase;
    private final SubmitReviewUseCase submitReviewUseCase;

    public ReviewScreen(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            ConsoleRouter router,
            EventManager eventManager,
            ReviewAssignmentGateway assignmentGateway,
            CommandExecutor commandExecutor,
            ListReviewerAssignmentsUseCase listReviewerAssignmentsUseCase,
            SubmitReviewUseCase submitReviewUseCase
    ) {
        super(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor);
        this.listReviewerAssignmentsUseCase = listReviewerAssignmentsUseCase;
        this.submitReviewUseCase = submitReviewUseCase;
    }

    @Override
    public void show() {
        printer.title("REVISOES");
        printer.menu("Minhas revisoes", "Concluir revisao", "Voltar");
        int option = reader.option("Escolha uma opcao", 1, 3);

        switch (option) {
            case 1 -> runSafely(this::myAssignments);
            case 2 -> runSafely(this::submitReview);
            case 3 -> {
            }
            default -> {
            }
        }
        router.navigateTo(ConsoleRouter.RESEARCHER_MENU);
    }

    private void myAssignments() {
        Event event = currentEvent();
        printer.section("MINHAS REVISOES");
        List<ReviewAssignment> assignments = executeCommand(
                () -> listReviewerAssignmentsUseCase.execute(event, session.currentUser()),
                session.currentUser().getEmail() + " CONSULTOU suas revisoes no evento " + event.getId()
        );
        printAssignments(assignments, true);
    }

    private void submitReview() {
        Event event = currentEvent();
        printer.section("CONCLUIR REVISAO");
        List<ReviewAssignment> pending = listReviewerAssignmentsUseCase.execute(event, session.currentUser()).stream()
                .filter(assignment -> !assignment.isFinished())
                .toList();

        if (pending.isEmpty()) {
            printer.empty("Voce nao possui revisoes pendentes.");
            return;
        }

        printAssignments(pending, true);
        int selected = reader.option("Escolha a revisao", 1, pending.size()) - 1;
        ReviewAssignment assignment = pending.get(selected);
        String contribution = reader.text("Principais contribuicoes");
        String criticism = reader.text("Pontos de critica");
        ReviewVerdict verdict = chooseVerdict();

        executeCommand(
                () -> submitReviewUseCase.execute(assignment.getPaper().getId(), session.currentUser(), contribution, criticism, verdict),
                session.currentUser().getEmail() + " ENVIOU parecer do artigo " + assignment.getPaper().getId()
        );
        printer.success("Revisao concluida.");
        printer.info("Se todas as revisoes do artigo terminaram, o autor foi notificado por email.");
    }
}
