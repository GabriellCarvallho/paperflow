package com.system.paperflow.presentation.console.screen;

import com.system.paperflow.application.command.CommandExecutor;
import com.system.paperflow.application.command.SimpleAuditableCommand;
import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.EventCategory;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.PaperStatus;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.domain.entity.ReviewVerdict;
import com.system.paperflow.domain.entity.ThematicArea;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public abstract class BaseConsoleScreen implements ConsoleScreen {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected final ConsoleReader reader;
    protected final ConsolePrinter printer;
    protected final ConsoleSession session;
    protected final ConsoleRouter router;
    protected final EventManager eventManager;
    protected final ReviewAssignmentGateway assignmentGateway;
    protected final CommandExecutor commandExecutor;

    protected BaseConsoleScreen(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            ConsoleRouter router,
            EventManager eventManager,
            ReviewAssignmentGateway assignmentGateway,
            CommandExecutor commandExecutor
    ) {
        this.reader = reader;
        this.printer = printer;
        this.session = session;
        this.router = router;
        this.eventManager = eventManager;
        this.assignmentGateway = assignmentGateway;
        this.commandExecutor = commandExecutor;
    }

    protected void runSafely(Runnable action) {
        try {
            action.run();
        } catch (Exception exception) {
            printer.error(exception.getMessage());
        }
        reader.pause();
    }

    protected Event currentEvent() {
        return eventManager.getCurrentEvent();
    }

    protected <T> T executeCommand(Supplier<T> action, String description) {
        return commandExecutor.execute(new SimpleAuditableCommand<>(action, description));
    }

    protected void printEventSummary() {
        if (!eventManager.hasEvent()) {
            printer.info("Evento atual: nenhum");
            return;
        }

        Event event = eventManager.getCurrentEvent();
        printer.info("Evento atual: " + event.getName() + " (" + event.getCategory() + ")");
        printer.info("Cidade: " + event.getCity() + " | Submissoes ate: " + format(event.getSubmissionDeadline()));
        printer.info("Status: " + (event.isStarted() ? "aberto/iniciado" : "em preparacao"));
    }

    protected EventCategory chooseCategory() {
        printer.menu("FULL_PAPER", "SHORT_PAPER", "DEMO");
        int option = reader.option("Categoria", 1, 3);
        return EventCategory.values()[option - 1];
    }

    protected ReviewVerdict chooseVerdict() {
        printer.menu("REJECTED", "WEAK_REJECTED", "WEAK_ACCEPTED", "ACCEPTED");
        int option = reader.option("Veredito", 1, 4);
        return ReviewVerdict.values()[option - 1];
    }

    protected void printThematicAreas(Event event) {
        if (event.getThematicAreas().isEmpty()) {
            printer.empty("Nenhuma area tematica cadastrada.");
            return;
        }

        List<List<String>> rows = new ArrayList<>();
        for (ThematicArea area : event.getThematicAreas()) {
            rows.add(List.of(area.name()));
        }
        printer.table(List.of("Areas tematicas"), rows);
    }

    protected void printPapers(List<Paper> papers) {
        if (papers.isEmpty()) {
            printer.empty("Nenhum artigo encontrado.");
            return;
        }

        List<List<String>> rows = papers.stream()
                .sorted(Comparator.comparing(Paper::getTitle))
                .map(paper -> List.of(
                        paper.getId().toString(),
                        paper.getTitle(),
                        paper.getStatus().name(),
                        paper.getAuthor().getEmail()
                ))
                .toList();
        printer.table(List.of("ID", "Titulo", "Status", "Autor"), rows);
    }

    protected void printAssignments(List<ReviewAssignment> assignments, boolean blind) {
        if (assignments.isEmpty()) {
            printer.empty("Nenhuma atribuicao encontrada.");
            return;
        }

        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < assignments.size(); i++) {
            ReviewAssignment assignment = assignments.get(i);
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(i + 1));
            row.add(assignment.getPaper().getTitle());
            row.add(assignment.getReviewer().getEmail());
            row.add(assignment.isFinished() ? "CONCLUIDA" : "PENDENTE");
            if (!blind) {
                row.add(assignment.getPaper().getAuthor().getEmail());
            }
            rows.add(row);
        }

        List<String> headers = blind
                ? List.of("#", "Artigo", "Revisor", "Status")
                : List.of("#", "Artigo", "Revisor", "Status", "Autor");
        printer.table(headers, rows);
    }

    protected void printPaperReviews(Paper paper) {
        List<ReviewAssignment> assignments = assignmentGateway.findByPaperId(paper.getId());
        List<ReviewAssignment> finished = assignments.stream().filter(ReviewAssignment::isFinished).toList();
        if (finished.isEmpty()) {
            printer.empty("Nenhum parecer disponivel.");
            return;
        }

        int index = 1;
        for (ReviewAssignment assignment : finished) {
            System.out.println("[Revisor " + index++ + "]");
            System.out.println("Veredito: " + assignment.getReview().getVerdict());
            System.out.println("Contribuicoes: " + assignment.getReview().getContribution());
            System.out.println("Criticas: " + assignment.getReview().getCriticism());
            System.out.println();
        }
    }

    protected boolean hasFinalDecision(Paper paper) {
        return paper.getStatus() == PaperStatus.ACCEPTED || paper.getStatus() == PaperStatus.REJECTED;
    }

    protected String summarize(String body) {
        String clean = body.replaceAll("<[^>]+>", " ")
                .replaceAll("\\s+", " ")
                .trim();
        return clean.length() <= 420 ? clean : clean.substring(0, 420) + "...";
    }

    private String format(LocalDate date) {
        return date == null ? "" : date.format(DATE_FORMAT);
    }
}
