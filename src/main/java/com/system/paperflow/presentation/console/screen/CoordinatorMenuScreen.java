package com.system.paperflow.presentation.console.screen;

import com.system.paperflow.application.command.CommandExecutor;
import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.usecase.distribute.DistributePapersUseCase;
import com.system.paperflow.application.usecase.event.StartEventUseCase;
import com.system.paperflow.application.usecase.paper.ListEventPapersUseCase;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;

public class CoordinatorMenuScreen extends BaseConsoleScreen {

    private final StartEventUseCase startEventUseCase;
    private final DistributePapersUseCase distributePapersUseCase;
    private final ListEventPapersUseCase listEventPapersUseCase;

    public CoordinatorMenuScreen(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            ConsoleRouter router,
            EventManager eventManager,
            ReviewAssignmentGateway assignmentGateway,
            CommandExecutor commandExecutor,
            StartEventUseCase startEventUseCase,
            DistributePapersUseCase distributePapersUseCase,
            ListEventPapersUseCase listEventPapersUseCase
    ) {
        super(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor);
        this.startEventUseCase = startEventUseCase;
        this.distributePapersUseCase = distributePapersUseCase;
        this.listEventPapersUseCase = listEventPapersUseCase;
    }

    @Override
    public void show() {
        Researcher user = session.currentUser();
        printer.title("MENU DO COORDENADOR");
        printer.info("Usuario: " + user.getEmail());
        printEventSummary();
        printer.menu(
                "Criar novo evento",
                "Cadastrar area tematica",
                "Adicionar revisor ao comite",
                "Iniciar recebimento de submissoes",
                "Distribuir artigos para revisao",
                "Listar artigos do evento",
                "Dashboard",
                "Ver emails registrados",
                "Sair da conta"
        );
        int option = reader.option("Escolha uma opcao", 1, 9);

        switch (option) {
            case 1 -> router.navigateTo(ConsoleRouter.EVENT);
            case 2 -> router.navigateTo(ConsoleRouter.THEMATIC_AREA);
            case 3 -> router.navigateTo(ConsoleRouter.COMMITTEE);
            case 4 -> runSafely(this::startEvent);
            case 5 -> runSafely(this::distributePapers);
            case 6 -> runSafely(this::listEventPapers);
            case 7 -> router.navigateTo(ConsoleRouter.DASHBOARD);
            case 8 -> router.navigateTo(ConsoleRouter.EMAIL);
            case 9 -> logout();
            default -> {
            }
        }
    }

    private void startEvent() {
        var event = currentEvent();
        executeCommand(
                () -> {
                    startEventUseCase.execute(event.getId());
                    return event;
                },
                session.currentUser().getEmail() + " INICIOU recebimento de submissoes do evento " + event.getId()
        );
        printer.success("Evento aberto para submissoes.");
    }

    private void distributePapers() {
        printer.section("DISTRIBUICAO AUTOMATICA");
        var event = currentEvent();
        var assignments = executeCommand(
                () -> distributePapersUseCase.execute(event.getId()),
                session.currentUser().getEmail() + " DISTRIBUIU artigos para revisao no evento " + event.getId()
        );
        if (assignments.isEmpty()) {
            printer.empty("Nenhum artigo foi distribuido.");
            return;
        }
        printer.success(assignments.size() + " atribuicao(oes) criada(s).");
        printAssignments(assignments, false);
    }

    private void listEventPapers() {
        printer.section("ARTIGOS DO EVENTO");
        var event = currentEvent();
        var papers = executeCommand(
                () -> listEventPapersUseCase.execute(event),
                session.currentUser().getEmail() + " CONSULTOU artigos do evento " + event.getId()
        );
        printPapers(papers);
    }

    private void logout() {
        String email = session.currentUser().getEmail();
        executeCommand(() -> email, "LOGOUT realizado por " + email);
        session.logout();
        router.navigateTo(ConsoleRouter.PUBLIC_MENU);
    }
}
