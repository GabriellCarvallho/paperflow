package com.system.paperflow.presentation.console.screen;

import com.system.paperflow.application.command.CommandExecutor;
import com.system.paperflow.application.usecase.user.LoginUserUseCase;
import com.system.paperflow.application.usecase.user.RegisterUserUseCase;
import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;

public class PublicMenuScreen extends BaseConsoleScreen {

    private final LoginUserUseCase loginUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;

    public PublicMenuScreen(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            ConsoleRouter router,
            EventManager eventManager,
            ReviewAssignmentGateway assignmentGateway,
            CommandExecutor commandExecutor,
            LoginUserUseCase loginUserUseCase,
            RegisterUserUseCase registerUserUseCase
    ) {
        super(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor);
        this.loginUserUseCase = loginUserUseCase;
        this.registerUserUseCase = registerUserUseCase;
    }

    @Override
    public void show() {
        printer.title("MENU INICIAL");
        printer.menu("Entrar", "Cadastrar pesquisador", "Sair");
        int option = reader.option("Escolha uma opcao", 1, 3);

        switch (option) {
            case 1 -> runSafely(this::login);
            case 2 -> runSafely(this::register);
            case 3 -> session.finish();
            default -> {
            }
        }
    }

    private void login() {
        printer.section("LOGIN");
        String email = reader.text("Email");
        String password = reader.text("Senha");
        Researcher researcher = executeCommand(
                () -> loginUserUseCase.execute(email, password),
                "LOGIN realizado por " + email
        );
        session.login(researcher);
        router.navigateTo(researcher.isCoordinator() ? ConsoleRouter.COORDINATOR_MENU : ConsoleRouter.RESEARCHER_MENU);
        printer.success("Login realizado.");
    }

    private void register() {
        printer.section("CADASTRO DE PESQUISADOR");
        String email = reader.text("Email");
        String password = reader.text("Senha");
        String institution = reader.text("Instituicao");
        Researcher researcher = executeCommand(
                () -> registerUserUseCase.execute(email, password, institution),
                "CADASTRO de pesquisador realizado para " + email
        );
        printer.success("Pesquisador cadastrado: " + researcher.getEmail());
    }
}
