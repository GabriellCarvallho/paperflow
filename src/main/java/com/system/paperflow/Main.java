package com.system.paperflow;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.factory.CoordinatorFactory;
import com.system.paperflow.application.factory.ResearcherFactory;
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
import com.system.paperflow.application.usecase.user.EnsureDefaultCoordinatorUseCase;
import com.system.paperflow.application.usecase.user.LoginUserUseCase;
import com.system.paperflow.application.usecase.user.RegisterUserUseCase;
import com.system.paperflow.infrastructure.memory.InMemoryEmailGateway;
import com.system.paperflow.infrastructure.memory.InMemoryPaperGateway;
import com.system.paperflow.infrastructure.memory.InMemoryReviewAssignmentGateway;
import com.system.paperflow.infrastructure.memory.InMemoryUserGateway;
import com.system.paperflow.presentation.console.ConsoleApp;
import com.system.paperflow.presentation.console.ConsolePrinter;
import com.system.paperflow.presentation.console.ConsoleReader;
import com.system.paperflow.presentation.console.ConsoleSession;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        EventManager eventManager = new EventManager();
        UserGateway userGateway = new InMemoryUserGateway();
        PaperGateway paperGateway = new InMemoryPaperGateway();
        ReviewAssignmentGateway assignmentGateway = new InMemoryReviewAssignmentGateway();
        InMemoryEmailGateway emailGateway = new InMemoryEmailGateway();

        PaperDecisionPublisher publisher = new PaperDecisionPublisher();
        publisher.addObserver(new AuthorNotificationObserver(emailGateway));

        ConsoleApp app = new ConsoleApp(
                new ConsoleReader(new Scanner(System.in)),
                new ConsolePrinter(),
                new ConsoleSession(),
                eventManager,
                new RegisterUserUseCase(userGateway, new ResearcherFactory()),
                new LoginUserUseCase(userGateway),
                new EnsureDefaultCoordinatorUseCase(userGateway, new CoordinatorFactory()),
                new CreateEventUseCase(eventManager),
                new StartEventUseCase(eventManager),
                new CreateThematicAreaUseCase(eventManager),
                new AddReviewerUseCase(eventManager, userGateway),
                new SubmitPaperUseCase(eventManager, paperGateway, userGateway),
                new ListAuthorPapersUseCase(paperGateway),
                new ListEventPapersUseCase(paperGateway),
                new DistributePapersUseCase(paperGateway, eventManager, assignmentGateway),
                new ListEventAssignmentsUseCase(assignmentGateway),
                new ListReviewerAssignmentsUseCase(assignmentGateway),
                new SubmitReviewUseCase(assignmentGateway, publisher),
                assignmentGateway,
                emailGateway
        );

        app.start();
    }
}
