package com.system.paperflow.presentation.console;

import com.system.paperflow.application.usecase.user.EnsureDefaultCoordinatorUseCase;

public class ConsoleApp {

    private final ConsolePrinter printer;
    private final ConsoleSession session;
    private final ConsoleRouter router;
    private final EnsureDefaultCoordinatorUseCase ensureDefaultCoordinatorUseCase;

    public ConsoleApp(
            ConsolePrinter printer,
            ConsoleSession session,
            EnsureDefaultCoordinatorUseCase ensureDefaultCoordinatorUseCase
    ) {
        this.printer = printer;
        this.session = session;
        this.ensureDefaultCoordinatorUseCase = ensureDefaultCoordinatorUseCase;
        this.router = new ConsoleRouter();
    }

    public ConsoleRouter getRouter() {
        return router;
    }

    public void start() {
        ensureDefaultCoordinatorUseCase.execute();
        printer.appHeader();

        while (session.isRunning()) {
            router.showCurrent();
        }
    }
}
