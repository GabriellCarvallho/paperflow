package com.system.paperflow.presentation.console.screen;

import com.system.paperflow.application.command.CommandExecutor;
import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.EmailGateway;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.domain.entity.EmailMessage;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;

import java.util.List;

public class EmailScreen extends BaseConsoleScreen {

    private final EmailGateway emailGateway;

    public EmailScreen(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            ConsoleRouter router,
            EventManager eventManager,
            ReviewAssignmentGateway assignmentGateway,
            CommandExecutor commandExecutor,
            EmailGateway emailGateway
    ) {
        super(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor);
        this.emailGateway = emailGateway;
    }

    @Override
    public void show() {
        runSafely(this::showEmails);
        router.navigateTo(session.currentUser().isCoordinator() ? ConsoleRouter.COORDINATOR_MENU : ConsoleRouter.RESEARCHER_MENU);
    }

    private void showEmails() {
        printer.section("EMAILS REGISTRADOS");
        List<EmailMessage> messages = executeCommand(
                emailGateway::sentMessages,
                session.currentUser().getEmail() + " CONSULTOU emails registrados"
        );
        if (messages.isEmpty()) {
            printer.empty("Nenhum email enviado.");
            return;
        }

        int index = 1;
        for (EmailMessage message : messages) {
            printer.info("#" + index++ + " Para: " + message.getTo());
            printer.info("Assunto: " + message.getSubject());
            String body = message.getBody() != null ? message.getBody() : message.getTemplate().template();
            System.out.println(summarize(body));
            System.out.println();
        }
    }
}
