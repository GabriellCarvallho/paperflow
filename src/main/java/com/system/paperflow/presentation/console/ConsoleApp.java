package com.system.paperflow.presentation.console;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
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
import com.system.paperflow.domain.entity.EmailMessage;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.EventCategory;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.PaperStatus;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.domain.entity.ReviewVerdict;
import com.system.paperflow.domain.entity.ThematicArea;
import com.system.paperflow.infrastructure.memory.InMemoryEmailGateway;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class ConsoleApp {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ConsoleReader reader;
    private final ConsolePrinter printer;
    private final ConsoleSession session;
    private final EventManager eventManager;
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final EnsureDefaultCoordinatorUseCase ensureDefaultCoordinatorUseCase;
    private final CreateEventUseCase createEventUseCase;
    private final StartEventUseCase startEventUseCase;
    private final CreateThematicAreaUseCase createThematicAreaUseCase;
    private final AddReviewerUseCase addReviewerUseCase;
    private final SubmitPaperUseCase submitPaperUseCase;
    private final ListAuthorPapersUseCase listAuthorPapersUseCase;
    private final ListEventPapersUseCase listEventPapersUseCase;
    private final DistributePapersUseCase distributePapersUseCase;
    private final ListEventAssignmentsUseCase listEventAssignmentsUseCase;
    private final ListReviewerAssignmentsUseCase listReviewerAssignmentsUseCase;
    private final SubmitReviewUseCase submitReviewUseCase;
    private final ReviewAssignmentGateway assignmentGateway;
    private final InMemoryEmailGateway emailGateway;

    public ConsoleApp(
            ConsoleReader reader,
            ConsolePrinter printer,
            ConsoleSession session,
            EventManager eventManager,
            RegisterUserUseCase registerUserUseCase,
            LoginUserUseCase loginUserUseCase,
            EnsureDefaultCoordinatorUseCase ensureDefaultCoordinatorUseCase,
            CreateEventUseCase createEventUseCase,
            StartEventUseCase startEventUseCase,
            CreateThematicAreaUseCase createThematicAreaUseCase,
            AddReviewerUseCase addReviewerUseCase,
            SubmitPaperUseCase submitPaperUseCase,
            ListAuthorPapersUseCase listAuthorPapersUseCase,
            ListEventPapersUseCase listEventPapersUseCase,
            DistributePapersUseCase distributePapersUseCase,
            ListEventAssignmentsUseCase listEventAssignmentsUseCase,
            ListReviewerAssignmentsUseCase listReviewerAssignmentsUseCase,
            SubmitReviewUseCase submitReviewUseCase,
            ReviewAssignmentGateway assignmentGateway,
            InMemoryEmailGateway emailGateway
    ) {
        this.reader = reader;
        this.printer = printer;
        this.session = session;
        this.eventManager = eventManager;
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.ensureDefaultCoordinatorUseCase = ensureDefaultCoordinatorUseCase;
        this.createEventUseCase = createEventUseCase;
        this.startEventUseCase = startEventUseCase;
        this.createThematicAreaUseCase = createThematicAreaUseCase;
        this.addReviewerUseCase = addReviewerUseCase;
        this.submitPaperUseCase = submitPaperUseCase;
        this.listAuthorPapersUseCase = listAuthorPapersUseCase;
        this.listEventPapersUseCase = listEventPapersUseCase;
        this.distributePapersUseCase = distributePapersUseCase;
        this.listEventAssignmentsUseCase = listEventAssignmentsUseCase;
        this.listReviewerAssignmentsUseCase = listReviewerAssignmentsUseCase;
        this.submitReviewUseCase = submitReviewUseCase;
        this.assignmentGateway = assignmentGateway;
        this.emailGateway = emailGateway;
    }

    public void start() {
        Researcher coordinator = ensureDefaultCoordinatorUseCase.execute();
        printer.appHeader();
        printer.info("Coordenador padrao: " + coordinator.getEmail() + " / " + EnsureDefaultCoordinatorUseCase.DEFAULT_PASSWORD);

        boolean running = true;
        while (running) {
            running = session.isAuthenticated() ? authenticatedMenu() : publicMenu();
        }
    }

    private boolean publicMenu() {
        printer.title("MENU INICIAL");
        printer.menu("Entrar", "Cadastrar pesquisador", "Ver credenciais do coordenador", "Sair");
        int option = reader.option("Escolha uma opcao", 1, 4);

        return switch (option) {
            case 1 -> action(this::login);
            case 2 -> action(this::register);
            case 3 -> action(this::showCoordinatorCredentials);
            case 4 -> false;
            default -> true;
        };
    }

    private boolean authenticatedMenu() {
        Researcher user = session.currentUser();
        if (user.isCoordinator()) {
            return coordinatorMenu();
        }
        return researcherMenu();
    }

    private boolean coordinatorMenu() {
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

        return switch (option) {
            case 1 -> action(this::createEvent);
            case 2 -> action(this::createThematicArea);
            case 3 -> action(this::addReviewer);
            case 4 -> action(this::startEvent);
            case 5 -> action(this::distributePapers);
            case 6 -> action(this::listEventPapers);
            case 7 -> action(this::dashboard);
            case 8 -> action(this::showEmails);
            case 9 -> action(() -> session.logout());
            default -> true;
        };
    }

    private boolean researcherMenu() {
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

        return switch (option) {
            case 1 -> action(this::submitPaper);
            case 2 -> action(this::myPapers);
            case 3 -> action(this::myAssignments);
            case 4 -> action(this::submitReview);
            case 5 -> action(this::showEmails);
            case 6 -> action(() -> session.logout());
            default -> true;
        };
    }

    private boolean action(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception exception) {
            printer.error(exception.getMessage());
        }
        reader.pause();
        return true;
    }

    private void login() {
        printer.section("LOGIN");
        String email = reader.text("Email");
        String password = reader.text("Senha");
        Researcher researcher = loginUserUseCase.execute(email, password);
        session.login(researcher);
        printer.success("Login realizado.");
    }

    private void register() {
        printer.section("CADASTRO DE PESQUISADOR");
        String email = reader.text("Email");
        String password = reader.text("Senha");
        String institution = reader.text("Instituicao");
        Researcher researcher = registerUserUseCase.execute(email, password, institution);
        printer.success("Pesquisador cadastrado: " + researcher.getEmail());
    }

    private void showCoordinatorCredentials() {
        printer.section("COORDENADOR PADRAO");
        printer.info("Email: " + EnsureDefaultCoordinatorUseCase.DEFAULT_EMAIL);
        printer.info("Senha: " + EnsureDefaultCoordinatorUseCase.DEFAULT_PASSWORD);
    }

    private void createEvent() {
        printer.section("CRIAR EVENTO");
        String name = reader.text("Nome do evento");
        String city = reader.text("Cidade");
        LocalDate endDate = reader.date("Data final do evento");
        LocalDate submissionDeadline = reader.date("Prazo final de submissao");
        EventCategory category = chooseCategory();

        Event event = createEventUseCase.execute(name, city, endDate, submissionDeadline, category);
        printer.success("Evento criado: " + event.getName());
        printer.info("ID: " + event.getId());
    }

    private void createThematicArea() {
        Event event = currentEvent();
        printer.section("CADASTRAR AREA TEMATICA");
        printThematicAreas(event);
        String areaName = reader.text("Nova area");
        ThematicArea area = createThematicAreaUseCase.execute(session.currentUser(), event.getId(), areaName);
        printer.success("Area cadastrada: " + area.name());
    }

    private void addReviewer() {
        Event event = currentEvent();
        printer.section("ADICIONAR REVISOR");
        printThematicAreas(event);
        String email = reader.text("Email do pesquisador cadastrado");
        List<String> areas = reader.csv("Areas de expertise separadas por virgula");
        Researcher reviewer = addReviewerUseCase.execute(session.currentUser(), email, areas);
        printer.success("Revisor adicionado ao comite: " + reviewer.getEmail());
    }

    private void startEvent() {
        Event event = currentEvent();
        startEventUseCase.execute(event.getId());
        printer.success("Evento aberto para submissoes.");
    }

    private void submitPaper() {
        Event event = currentEvent();
        printer.section("SUBMISSAO DE ARTIGO");
        printThematicAreas(event);
        String title = reader.text("Titulo do artigo");
        String summary = reader.text("Resumo");
        List<String> areas = reader.csv("Areas do artigo separadas por virgula");
        List<String> collaborators = reader.csv("Emails dos coautores separados por virgula");

        Paper paper = submitPaperUseCase.execute(session.currentUser(), event.getId(), title, summary, areas, collaborators);
        printer.success("Artigo submetido.");
        printer.info("ID: " + paper.getId());
        printer.info("Status: " + paper.getStatus());
    }

    private void distributePapers() {
        Event event = currentEvent();
        printer.section("DISTRIBUICAO AUTOMATICA");
        List<ReviewAssignment> assignments = distributePapersUseCase.execute(event.getId());
        if (assignments.isEmpty()) {
            printer.empty("Nenhum artigo foi distribuido.");
            return;
        }
        printer.success(assignments.size() + " atribuicao(oes) criada(s).");
        printAssignments(assignments, false);
    }

    private void listEventPapers() {
        Event event = currentEvent();
        printer.section("ARTIGOS DO EVENTO");
        List<Paper> papers = listEventPapersUseCase.execute(event);
        printPapers(papers);
    }

    private void myPapers() {
        printer.section("MEUS ARTIGOS");
        List<Paper> papers = listAuthorPapersUseCase.execute(session.currentUser().getEmail());
        printPapers(papers);
        for (Paper paper : papers) {
            if (paper.getStatus() == PaperStatus.ACCEPTED || paper.getStatus() == PaperStatus.REJECTED) {
                printer.section("PARECERES - " + paper.getTitle());
                printPaperReviews(paper);
            }
        }
    }

    private void myAssignments() {
        Event event = currentEvent();
        printer.section("MINHAS REVISOES");
        List<ReviewAssignment> assignments = listReviewerAssignmentsUseCase.execute(event, session.currentUser());
        printAssignments(assignments, true);
    }

    private void submitReview() {
        Event event = currentEvent();
        printer.section("CONCLUIR REVISAO");
        List<ReviewAssignment> pending = listReviewerAssignmentsUseCase.execute(event, session.currentUser()).stream()
                .filter(assignment -> !assignment.isFinished())
                .toList();

        if (pending.isEmpty()) {
            printer.empty("Voce nao possui revisoes pendentes.");
            return;
        }

        printAssignments(pending, true);
        int selected = reader.option("Escolha a revisao", 1, pending.size()) - 1;
        ReviewAssignment assignment = pending.get(selected);
        String contribution = reader.text("Principais contribuicoes");
        String criticism = reader.text("Pontos de critica");
        ReviewVerdict verdict = chooseVerdict();

        submitReviewUseCase.execute(assignment.getPaper().getId(), session.currentUser(), contribution, criticism, verdict);
        printer.success("Revisao concluida.");
        printer.info("Se todas as revisoes do artigo terminaram, o autor foi notificado por email.");
    }

    private void dashboard() {
        Event event = currentEvent();
        printer.section("DASHBOARD");
        List<Paper> papers = listEventPapersUseCase.execute(event);
        List<ReviewAssignment> assignments = listEventAssignmentsUseCase.execute(event);
        long evaluatedPapers = papers.stream()
                .filter(paper -> paper.getStatus() == PaperStatus.ACCEPTED || paper.getStatus() == PaperStatus.REJECTED)
                .count();
        long pendingAssignments = assignments.stream().filter(assignment -> !assignment.isFinished()).count();

        printer.table(
                List.of("Indicador", "Valor"),
                List.of(
                        List.of("Artigos submetidos", String.valueOf(papers.size())),
                        List.of("Revisores no comite", String.valueOf(event.getCommittee().size())),
                        List.of("Artigos avaliados", String.valueOf(evaluatedPapers)),
                        List.of("Revisoes pendentes", String.valueOf(pendingAssignments))
                )
        );

        List<ReviewAssignment> pending = assignments.stream()
                .filter(assignment -> !assignment.isFinished())
                .toList();
        if (!pending.isEmpty()) {
            printer.section("PENDENCIAS");
            printAssignments(pending, false);
        }
    }

    private void showEmails() {
        printer.section("EMAILS REGISTRADOS");
        List<EmailMessage> messages = emailGateway.sentMessages();
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

    private EventCategory chooseCategory() {
        printer.menu("FULL_PAPER", "SHORT_PAPER", "DEMO");
        int option = reader.option("Categoria", 1, 3);
        return EventCategory.values()[option - 1];
    }

    private ReviewVerdict chooseVerdict() {
        printer.menu("REJECTED", "WEAK_REJECTED", "WEAK_ACCEPTED", "ACCEPTED");
        int option = reader.option("Veredito", 1, 4);
        return ReviewVerdict.values()[option - 1];
    }

    private void printEventSummary() {
        if (!eventManager.hasEvent()) {
            printer.info("Evento atual: nenhum");
            return;
        }

        Event event = eventManager.getCurrentEvent();
        printer.info("Evento atual: " + event.getName() + " (" + event.getCategory() + ")");
        printer.info("Cidade: " + event.getCity() + " | Submissoes ate: " + format(event.getSubmissionDeadline()));
        printer.info("Status: " + (event.isStarted() ? "aberto/iniciado" : "em preparacao"));
    }

    private void printThematicAreas(Event event) {
        if (event.getThematicAreas().isEmpty()) {
            printer.empty("Nenhuma area tematica cadastrada.");
            return;
        }

        List<List<String>> rows = new ArrayList<>();
        for (ThematicArea area : event.getThematicAreas()) {
            rows.add(List.of(area.name()));
        }
        printer.table(List.of("Areas tematicas"), rows);
    }

    private void printPapers(List<Paper> papers) {
        if (papers.isEmpty()) {
            printer.empty("Nenhum artigo encontrado.");
            return;
        }

        List<List<String>> rows = papers.stream()
                .sorted(Comparator.comparing(Paper::getTitle))
                .map(paper -> List.of(
                        paper.getId().toString(),
                        paper.getTitle(),
                        paper.getStatus().name(),
                        paper.getAuthor().getEmail()
                ))
                .toList();
        printer.table(List.of("ID", "Titulo", "Status", "Autor"), rows);
    }

    private void printAssignments(List<ReviewAssignment> assignments, boolean blind) {
        if (assignments.isEmpty()) {
            printer.empty("Nenhuma atribuicao encontrada.");
            return;
        }

        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < assignments.size(); i++) {
            ReviewAssignment assignment = assignments.get(i);
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(i + 1));
            row.add(assignment.getPaper().getTitle());
            row.add(assignment.getReviewer().getEmail());
            row.add(assignment.isFinished() ? "CONCLUIDA" : "PENDENTE");
            if (!blind) {
                row.add(assignment.getPaper().getAuthor().getEmail());
            }
            rows.add(row);
        }

        List<String> headers = blind
                ? List.of("#", "Artigo", "Revisor", "Status")
                : List.of("#", "Artigo", "Revisor", "Status", "Autor");
        printer.table(headers, rows);
    }

    private void printPaperReviews(Paper paper) {
        List<ReviewAssignment> assignments = assignmentGateway.findByPaperId(paper.getId());
        List<ReviewAssignment> finished = assignments.stream().filter(ReviewAssignment::isFinished).toList();
        if (finished.isEmpty()) {
            printer.empty("Nenhum parecer disponivel.");
            return;
        }

        int index = 1;
        for (ReviewAssignment assignment : finished) {
            System.out.println("[Revisor " + index++ + "]");
            System.out.println("Veredito: " + assignment.getReview().getVerdict());
            System.out.println("Contribuicoes: " + assignment.getReview().getContribution());
            System.out.println("Criticas: " + assignment.getReview().getCriticism());
            System.out.println();
        }
    }

    private Event currentEvent() {
        return eventManager.getCurrentEvent();
    }

    private String format(LocalDate date) {
        return date == null ? "" : date.format(DATE_FORMAT);
    }

    private String summarize(String body) {
        String clean = body.replaceAll("<[^>]+>", " ")
                .replaceAll("\\s+", " ")
                .trim();
        return clean.length() <= 420 ? clean : clean.substring(0, 420) + "...";
    }
}
