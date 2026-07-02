package com.system.paperflow.presentation.console.screen;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;

public class ResearcherMenuScreen extends BaseConsoleScreen {

    public ResearcherMenuScreen(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            ConsoleRouter router,
            EventManager eventManager,
            ReviewAssignmentGateway assignmentGateway
    ) {
        super(reader, printer, session, router, eventManager, assignmentGateway);
    }

    @Override
    public void show() {
        Researcher user = session.currentUser();
        printer.title("MENU DO PESQUISADOR");
        printer.info("Usuario: " + user.getEmail());
        printEventSummary();
        printer.menu(
                "Submeter artigo",
                "Meus artigos",
                "Minhas revisoes",
                "Concluir revisao",
                "Ver emails registrados",
                "Sair da conta"
        );
        int option = reader.option("Escolha uma opcao", 1, 6);

        switch (option) {
            case 1, 2 -> router.navigateTo(ConsoleRouter.PAPER);
            case 3, 4 -> router.navigateTo(ConsoleRouter.REVIEW);
            case 5 -> router.navigateTo(ConsoleRouter.EMAIL);
            case 6 -> logout();
            default -> {
            }
        }
    }

    private void logout() {
        session.logout();
        router.navigateTo(ConsoleRouter.PUBLIC_MENU);
    }
}
