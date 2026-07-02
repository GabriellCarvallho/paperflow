package com.system.paperflow.presentation.console.screen;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.usecase.paper.ListAuthorPapersUseCase;
import com.system.paperflow.application.usecase.paper.SubmitPaperUseCase;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;

import java.util.List;

public class PaperScreen extends BaseConsoleScreen {

    private final SubmitPaperUseCase submitPaperUseCase;
    private final ListAuthorPapersUseCase listAuthorPapersUseCase;

    public PaperScreen(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            ConsoleRouter router,
            EventManager eventManager,
            ReviewAssignmentGateway assignmentGateway,
            SubmitPaperUseCase submitPaperUseCase,
            ListAuthorPapersUseCase listAuthorPapersUseCase
    ) {
        super(reader, printer, session, router, eventManager, assignmentGateway);
        this.submitPaperUseCase = submitPaperUseCase;
        this.listAuthorPapersUseCase = listAuthorPapersUseCase;
    }

    @Override
    public void show() {
        printer.title("ARTIGOS");
        printer.menu("Submeter artigo", "Meus artigos", "Voltar");
        int option = reader.option("Escolha uma opcao", 1, 3);

        switch (option) {
            case 1 -> runSafely(this::submitPaper);
            case 2 -> runSafely(this::myPapers);
            case 3 -> {
            }
            default -> {
            }
        }
        router.navigateTo(ConsoleRouter.RESEARCHER_MENU);
    }

    private void submitPaper() {
        Event event = currentEvent();
        printer.section("SUBMISSAO DE ARTIGO");
        printThematicAreas(event);
        String title = reader.text("Titulo do artigo");
        String summary = reader.text("Resumo");
        List<String> areas = reader.csv("Areas do artigo separadas por virgula");
        List<String> collaborators = reader.csv("Emails dos coautores separados por virgula");

        Paper paper = submitPaperUseCase.execute(session.currentUser(), event.getId(), title, summary, areas, collaborators);
        printer.success("Artigo submetido.");
        printer.info("ID: " + paper.getId());
        printer.info("Status: " + paper.getStatus());
    }

    private void myPapers() {
        printer.section("MEUS ARTIGOS");
        List<Paper> papers = listAuthorPapersUseCase.execute(session.currentUser().getEmail());
        printPapers(papers);
        for (Paper paper : papers) {
            if (hasFinalDecision(paper)) {
                printer.section("PARECERES - " + paper.getTitle());
                printPaperReviews(paper);
            }
        }
    }
}
