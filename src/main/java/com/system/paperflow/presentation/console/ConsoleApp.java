package com.system.paperflow.presentation.console;

import com.system.paperflow.application.usecase.user.SeedMockDataUseCase;

public class ConsoleApp {

    private final ConsolePrinter printer;
    private final ConsoleSession session;
    private final ConsoleRouter router;
    private final SeedMockDataUseCase seedMockDataUseCase;

    public ConsoleApp(
            ConsolePrinter printer,
            ConsoleSession session,
            SeedMockDataUseCase seedMockDataUseCase
    ) {
        this.printer = printer;
        this.session = session;
        this.seedMockDataUseCase = seedMockDataUseCase;
        this.router = new ConsoleRouter();
    }

    public ConsoleRouter getRouter() {
        return router;
    }

    public void start() {
        seedMockDataUseCase.execute();
        printer.appHeader();

        while (session.isRunning()) {
            router.showCurrent();
        }
    }
}
