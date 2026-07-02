package com.system.paperflow;

public class Main {

    public static void main(String[] args) {

//        UserGateway userGateway = new LocalUserGateway();
//        EventGateway eventPersistence = new LocalEventGateway();
//        ThematicGateway thematicGateway = new LocalThematicGateway();
//        CommitteeGateway committeeGateway = new LocalCommitteeGateway();
//        PaperGateway paperGateway = new LocalPaperGateway();
//        ScreenContext screenContext = new ScreenContext();
//        CommitteeInvitationPublisher committeePublisher = new CommitteeInvitationPublisher();
//        committeePublisher.subscribe(new CommitteeAuditTrailObserver());
//
//        new EnsureDefaultCoordinatorUseCase(userGateway, new CoordinatorFactory()).execute();
//
//        ResearcherFactory researcherFactory = new ResearcherFactory();
//
//        RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(userGateway, researcherFactory);
//        LoginUserUseCase loginUserUseCase = new LoginUserUseCase(userGateway);
//        CreateEventUseCase createEventUseCase = new CreateEventUseCase(eventPersistence);
//        ListEventsUseCase listEventsUseCase = new ListEventsUseCase(eventPersistence);
//        StartEventUseCase startEventUseCase = new StartEventUseCase(eventPersistence);
//        CreateThematicAreaUseCase createThematicAreaUseCase = new CreateThematicAreaUseCase(thematicGateway, userGateway);
//        ListTopicTreeUseCase listTopicTreeUseCase = new ListTopicTreeUseCase(thematicGateway);
//        InviteReviewerUseCase inviteReviewerUseCase = new InviteReviewerUseCase(userGateway, committeeGateway, committeePublisher);
//        ListCommitteeInvitationsUseCase listCommitteeInvitationsUseCase = new ListCommitteeInvitationsUseCase(committeeGateway);
//        ListTechnicalCommitteeUseCase listTechnicalCommitteeUseCase = new ListTechnicalCommitteeUseCase(committeeGateway);
//        ListEventPapersUseCase listEventPapersUseCase = new ListEventPapersUseCase(paperGateway);
//        ListEventAssignmentsUseCase listEventAssignmentsUseCase = new ListEventAssignmentsUseCase(paperGateway);
//        DashboardUseCase dashboardUseCase = new DashboardUseCase();
//
//        Supplier<Screen> loginSupplier = () -> new LoginScreen(loginUserUseCase, screenContext);
//        Supplier<Screen> registerSupplier = () -> new RegisterScreen(registerUserUseCase);
//        Supplier<Screen> eventsSupplier = () -> new EventsScreen(screenContext, listEventsUseCase);
//        Supplier<Screen> eventSupplier = () -> new EventBlankScreen(screenContext, listEventPapersUseCase, listEventAssignmentsUseCase);
//        Supplier<Screen> createEventSupplier = () -> new CreateEventScreen(createEventUseCase);
//        Supplier<Screen> topicsSupplier = () -> new TopicManagementScreen(screenContext, createThematicAreaUseCase, listTopicTreeUseCase);
//        Supplier<Screen> invitationsSupplier = () -> new ReviewerInvitationScreen(screenContext, inviteReviewerUseCase, listCommitteeInvitationsUseCase, startEventUseCase);
//        Supplier<Screen> dashboardSupplier = () -> new DashboardScreen(screenContext, dashboardUseCase, listEventPapersUseCase, listTechnicalCommitteeUseCase, listEventAssignmentsUseCase);
//
//        ScreenUtils.register("login", loginSupplier);
//        ScreenUtils.register("register", registerSupplier);
//        ScreenUtils.register("dashboard", dashboardSupplier);
//        ScreenUtils.register("events", eventsSupplier);
//        ScreenUtils.register("event", eventSupplier);
//        ScreenUtils.register("event-create", createEventSupplier);
//        ScreenUtils.register("topics", topicsSupplier);
//        ScreenUtils.register("reviewer-invitations", invitationsSupplier);
//
//        ScreenUtils.start("login");
    }
    
}
