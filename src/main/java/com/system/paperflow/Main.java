package com.system.paperflow;

import com.system.paperflow.application.command.CommandExecutor;
import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.factory.CoordinatorFactory;
import com.system.paperflow.application.factory.EmailTemplateFactory;
import com.system.paperflow.application.factory.ResearcherFactory;
import com.system.paperflow.application.gateway.AuditLogGateway;
import com.system.paperflow.application.gateway.EmailGateway;
import com.system.paperflow.application.gateway.PaperGateway;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.gateway.UserGateway;
import com.system.paperflow.application.observer.AuthorNotificationObserver;
import com.system.paperflow.application.observer.publisher.PaperDecisionPublisher;
import com.system.paperflow.application.usecase.committee.AddReviewerUseCase;
import com.system.paperflow.application.usecase.distribute.DistributePapersUseCase;
import com.system.paperflow.application.usecase.event.CreateEventUseCase;
import com.system.paperflow.application.usecase.event.StartEventUseCase;
import com.system.paperflow.application.usecase.paper.ListAuthorPapersUseCase;
import com.system.paperflow.application.usecase.paper.ListEventPapersUseCase;
import com.system.paperflow.application.usecase.paper.SubmitPaperUseCase;
import com.system.paperflow.application.usecase.review.ListEventAssignmentsUseCase;
import com.system.paperflow.application.usecase.review.ListReviewerAssignmentsUseCase;
import com.system.paperflow.application.usecase.review.SubmitReviewUseCase;
import com.system.paperflow.application.usecase.thematic.CreateThematicAreaUseCase;
import com.system.paperflow.application.usecase.user.LoginUserUseCase;
import com.system.paperflow.application.usecase.user.RegisterUserUseCase;
import com.system.paperflow.application.usecase.user.SeedMockDataUseCase;
import com.system.paperflow.application.strategy.distribution.CompatibilityDistributionStrategy;
import com.system.paperflow.infrastructure.gateway.InMemoryPaperGateway;
import com.system.paperflow.infrastructure.gateway.InMemoryReviewAssignmentGateway;
import com.system.paperflow.infrastructure.gateway.InMemoryUserGateway;
import com.system.paperflow.infrastructure.gateway.JakartaMailGateway;
import com.system.paperflow.infrastructure.gateway.FileAuditLogGateway;
import com.system.paperflow.presentation.console.ConsoleApp;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleRouter;
import com.system.paperflow.presentation.console.ConsoleSession;
import com.system.paperflow.presentation.console.screen.CommitteeScreen;
import com.system.paperflow.presentation.console.screen.CoordinatorMenuScreen;
import com.system.paperflow.presentation.console.screen.DashboardScreen;
import com.system.paperflow.presentation.console.screen.EmailScreen;
import com.system.paperflow.presentation.console.screen.EventScreen;
import com.system.paperflow.presentation.console.screen.PaperScreen;
import com.system.paperflow.presentation.console.screen.PublicMenuScreen;
import com.system.paperflow.presentation.console.screen.ResearcherMenuScreen;
import com.system.paperflow.presentation.console.screen.ReviewScreen;
import com.system.paperflow.presentation.console.screen.ThematicAreaScreen;

import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        int port = Integer.parseInt(dotenv.get("PORT"));
        String host = dotenv.get("HOST");
        String username = dotenv.get("EMAIL_USERNAME");
        String password = dotenv.get("PASSWORD");

        EventManager eventManager = new EventManager();
        UserGateway userGateway = new InMemoryUserGateway();
        PaperGateway paperGateway = new InMemoryPaperGateway();
        ReviewAssignmentGateway assignmentGateway = new InMemoryReviewAssignmentGateway();
        AuditLogGateway auditLogGateway = new FileAuditLogGateway(Path.of("data", "audit.log"));
        CommandExecutor commandExecutor = new CommandExecutor(auditLogGateway);

        EmailGateway emailGateway = JakartaMailGateway.builder().withHost(host).withPassword(password).withPort(port).withUsername(username).build();


        PaperDecisionPublisher publisher = new PaperDecisionPublisher();
        publisher.addObserver(new AuthorNotificationObserver(emailGateway, new EmailTemplateFactory()));

        ConsoleReader reader = new ConsoleReader(new Scanner(System.in));
        ConsolePrinter printer = new ConsolePrinter();
        ConsoleSession session = new ConsoleSession();

        RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(userGateway, new ResearcherFactory());
        LoginUserUseCase loginUserUseCase = new LoginUserUseCase(userGateway);
        SeedMockDataUseCase seedMockDataUseCase = new SeedMockDataUseCase(
                eventManager,
                userGateway,
                paperGateway,
                assignmentGateway,
                new CoordinatorFactory(),
                new ResearcherFactory()
        );
        CreateEventUseCase createEventUseCase = new CreateEventUseCase(eventManager);
        StartEventUseCase startEventUseCase = new StartEventUseCase(eventManager);
        CreateThematicAreaUseCase createThematicAreaUseCase = new CreateThematicAreaUseCase(eventManager);
        AddReviewerUseCase addReviewerUseCase = new AddReviewerUseCase(eventManager, userGateway);
        SubmitPaperUseCase submitPaperUseCase = new SubmitPaperUseCase(eventManager, paperGateway, userGateway);
        ListAuthorPapersUseCase listAuthorPapersUseCase = new ListAuthorPapersUseCase(paperGateway);
        ListEventPapersUseCase listEventPapersUseCase = new ListEventPapersUseCase(paperGateway);
        DistributePapersUseCase distributePapersUseCase = new DistributePapersUseCase(
                paperGateway,
                eventManager,
                assignmentGateway,
                new CompatibilityDistributionStrategy()
        );
        ListEventAssignmentsUseCase listEventAssignmentsUseCase = new ListEventAssignmentsUseCase(assignmentGateway);
        ListReviewerAssignmentsUseCase listReviewerAssignmentsUseCase = new ListReviewerAssignmentsUseCase(assignmentGateway);
        SubmitReviewUseCase submitReviewUseCase = new SubmitReviewUseCase(assignmentGateway, publisher);

        ConsoleApp app = new ConsoleApp(printer, session, seedMockDataUseCase);
        ConsoleRouter router = app.getRouter();

        router.register(ConsoleRouter.PUBLIC_MENU, new PublicMenuScreen(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor, loginUserUseCase, registerUserUseCase));
        router.register(ConsoleRouter.COORDINATOR_MENU, new CoordinatorMenuScreen(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor, startEventUseCase, distributePapersUseCase, listEventPapersUseCase));
        router.register(ConsoleRouter.RESEARCHER_MENU, new ResearcherMenuScreen(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor));
        router.register(ConsoleRouter.EVENT, new EventScreen(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor, createEventUseCase));
        router.register(ConsoleRouter.THEMATIC_AREA, new ThematicAreaScreen(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor, createThematicAreaUseCase));
        router.register(ConsoleRouter.COMMITTEE, new CommitteeScreen(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor, addReviewerUseCase));
        router.register(ConsoleRouter.PAPER, new PaperScreen(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor, submitPaperUseCase, listAuthorPapersUseCase));
        router.register(ConsoleRouter.REVIEW, new ReviewScreen(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor, listReviewerAssignmentsUseCase, submitReviewUseCase));
        router.register(ConsoleRouter.DASHBOARD, new DashboardScreen(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor, listEventPapersUseCase, listEventAssignmentsUseCase));
        router.register(ConsoleRouter.EMAIL, new EmailScreen(reader, printer, session, router, eventManager, assignmentGateway, commandExecutor, emailGateway));

        app.start();
    }
}
