package com.system.paperflow;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.factory.CoordinatorFactory;
import com.system.paperflow.application.factory.ResearcherFactory;
import com.system.paperflow.application.gateway.PaperGateway;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.gateway.UserGateway;
import com.system.paperflow.application.usecase.committee.AddReviewerUseCase;
import com.system.paperflow.application.usecase.distribute.DistributePapersUseCase;
import com.system.paperflow.application.usecase.event.CreateEventUseCase;
import com.system.paperflow.application.usecase.event.StartEventUseCase;
import com.system.paperflow.application.usecase.notification.NotifyAuthorsUseCase;
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
import com.system.paperflow.presentation.context.ScreenContext;
import com.system.paperflow.presentation.screen.AuthorNotificationScreen;
import com.system.paperflow.presentation.screen.CommitteeManagementScreen;
import com.system.paperflow.presentation.screen.CreateEventScreen;
import com.system.paperflow.presentation.screen.DashboardScreen;
import com.system.paperflow.presentation.screen.DistributionScreen;
import com.system.paperflow.presentation.screen.EventScreen;
import com.system.paperflow.presentation.screen.EventsScreen;
import com.system.paperflow.presentation.screen.LoginScreen;
import com.system.paperflow.presentation.screen.MyPapersScreen;
import com.system.paperflow.presentation.screen.RegisterScreen;
import com.system.paperflow.presentation.screen.RequirementTenScreen;
import com.system.paperflow.presentation.screen.ReviewerAssignmentsScreen;
import com.system.paperflow.presentation.screen.SubmitPaperScreen;
import com.system.paperflow.presentation.screen.SubmitReviewScreen;
import com.system.paperflow.presentation.screen.TopicManagementScreen;
import com.system.paperflow.presentation.ui.ScreenUtils;

public class Main {

    public static void main(String[] args) {
        EventManager eventManager = new EventManager();
        UserGateway userGateway = new InMemoryUserGateway();
        PaperGateway paperGateway = new InMemoryPaperGateway();
        ReviewAssignmentGateway assignmentGateway = new InMemoryReviewAssignmentGateway();
        InMemoryEmailGateway emailGateway = new InMemoryEmailGateway();
        ScreenContext screenContext = new ScreenContext();

        new EnsureDefaultCoordinatorUseCase(userGateway, new CoordinatorFactory()).execute();

        RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(userGateway, new ResearcherFactory());
        LoginUserUseCase loginUserUseCase = new LoginUserUseCase(userGateway);
        CreateEventUseCase createEventUseCase = new CreateEventUseCase(eventManager);
        StartEventUseCase startEventUseCase = new StartEventUseCase(eventManager);
        CreateThematicAreaUseCase createThematicAreaUseCase = new CreateThematicAreaUseCase(eventManager);
        AddReviewerUseCase addReviewerUseCase = new AddReviewerUseCase(eventManager, userGateway);
        SubmitPaperUseCase submitPaperUseCase = new SubmitPaperUseCase(eventManager, paperGateway, userGateway);
        ListAuthorPapersUseCase listAuthorPapersUseCase = new ListAuthorPapersUseCase(paperGateway);
        ListEventPapersUseCase listEventPapersUseCase = new ListEventPapersUseCase(paperGateway);
        DistributePapersUseCase distributePapersUseCase = new DistributePapersUseCase(paperGateway, eventManager, assignmentGateway);
        SubmitReviewUseCase submitReviewUseCase = new SubmitReviewUseCase(assignmentGateway);
        ListEventAssignmentsUseCase listEventAssignmentsUseCase = new ListEventAssignmentsUseCase(assignmentGateway);
        ListReviewerAssignmentsUseCase listReviewerAssignmentsUseCase = new ListReviewerAssignmentsUseCase(assignmentGateway);
        NotifyAuthorsUseCase notifyAuthorsUseCase = new NotifyAuthorsUseCase(eventManager, paperGateway, assignmentGateway, emailGateway);

        ScreenUtils.register("login", () -> new LoginScreen(loginUserUseCase, screenContext));
        ScreenUtils.register("register", () -> new RegisterScreen(registerUserUseCase));
        ScreenUtils.register("events", () -> new EventsScreen(screenContext, eventManager));
        ScreenUtils.register("event-create", () -> new CreateEventScreen(createEventUseCase));
        ScreenUtils.register("event", () -> new EventScreen(screenContext, eventManager, startEventUseCase, listEventPapersUseCase, listEventAssignmentsUseCase));
        ScreenUtils.register("topics", () -> new TopicManagementScreen(screenContext, eventManager, createThematicAreaUseCase));
        ScreenUtils.register("committee", () -> new CommitteeManagementScreen(screenContext, eventManager, addReviewerUseCase));
        ScreenUtils.register("submit-paper", () -> new SubmitPaperScreen(screenContext, eventManager, submitPaperUseCase));
        ScreenUtils.register("my-papers", () -> new MyPapersScreen(screenContext, listAuthorPapersUseCase, assignmentGateway));
        ScreenUtils.register("distribution", () -> new DistributionScreen(eventManager, distributePapersUseCase, listEventAssignmentsUseCase));
        ScreenUtils.register("reviewer-assignments", () -> new ReviewerAssignmentsScreen(screenContext, eventManager, listReviewerAssignmentsUseCase));
        ScreenUtils.register("submit-review", () -> new SubmitReviewScreen(screenContext, submitReviewUseCase));
        ScreenUtils.register("dashboard", () -> new DashboardScreen(eventManager, listEventPapersUseCase, listEventAssignmentsUseCase));
        ScreenUtils.register("notifications", () -> new AuthorNotificationScreen(notifyAuthorsUseCase, emailGateway));
        ScreenUtils.register("rf10", RequirementTenScreen::new);

        ScreenUtils.start("login");
    }
}
