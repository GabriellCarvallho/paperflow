package com.system.paperflow.application.usecase.user;

import com.system.paperflow.application.event.EventManager;
import com.system.paperflow.application.factory.CoordinatorFactory;
import com.system.paperflow.application.factory.ResearcherFactory;
import com.system.paperflow.application.gateway.PaperGateway;
import com.system.paperflow.application.gateway.ReviewAssignmentGateway;
import com.system.paperflow.application.gateway.UserGateway;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.EventCategory;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.Review;
import com.system.paperflow.domain.entity.ReviewAssignment;
import com.system.paperflow.domain.entity.ReviewVerdict;
import com.system.paperflow.domain.entity.ThematicArea;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SeedMockDataUseCase {

    public static final String DEFAULT_USERNAME = "Coordenador Geral";
    public static final String DEFAULT_EMAIL = "coordenador@paperflow.com";
    public static final String DEFAULT_PASSWORD = "admin123";
    public static final String DEFAULT_INSTITUTION = "PaperFlow";

    private final EventManager eventManager;
    private final UserGateway userGateway;
    private final PaperGateway paperGateway;
    private final ReviewAssignmentGateway assignmentGateway;
    private final CoordinatorFactory coordinatorFactory;
    private final ResearcherFactory researcherFactory;

    public SeedMockDataUseCase(
            EventManager eventManager,
            UserGateway userGateway,
            PaperGateway paperGateway,
            ReviewAssignmentGateway assignmentGateway,
            CoordinatorFactory coordinatorFactory,
            ResearcherFactory researcherFactory
    ) {
        this.eventManager = eventManager;
        this.userGateway = userGateway;
        this.paperGateway = paperGateway;
        this.assignmentGateway = assignmentGateway;
        this.coordinatorFactory = coordinatorFactory;
        this.researcherFactory = researcherFactory;
    }

    public Researcher execute() {
        Researcher coordinator = ensureDefaultCoordinator();

        if (eventManager.hasEvent()) {
            return coordinator;
        }

        List<Researcher> researchers = seedResearchers();
        Event event = seedEvent();
        seedThematicAreas(event);
        seedCommittee(event, researchers.subList(0, 5));
        event.start();

        List<Paper> papers = seedPapers(event, researchers.subList(5, 10));
        List<ReviewAssignment> assignments = seedAssignments(papers, researchers.subList(0, 5));
        seedReviews(assignments);

        return coordinator;
    }

    private Researcher ensureDefaultCoordinator() {
        return userGateway.findByEmail(DEFAULT_EMAIL)
                .map(this::validateCoordinator)
                .orElseGet(this::createDefaultCoordinator);
    }

    private Researcher validateCoordinator(Researcher researcher) {
        if (!researcher.isCoordinator()) {
            throw new RuntimeException(
                    "O email do coordenador padrao ja esta cadastrado, mas nao pertence a um coordenador."
            );
        }

        return researcher;
    }

    private Researcher createDefaultCoordinator() {
        Researcher coordinator = coordinatorFactory.create(
                DEFAULT_USERNAME,
                DEFAULT_EMAIL,
                DEFAULT_PASSWORD,
                DEFAULT_INSTITUTION
        );

        userGateway.save(coordinator);
        return coordinator;
    }

    private List<Researcher> seedResearchers() {
        List<Researcher> researchers = new ArrayList<>();

        for (int index = 1; index <= 10; index++) {
            int currentIndex = index;
            String email = "pesquisador%02d@paperflow.com".formatted(index);
            Researcher researcher = userGateway.findByEmail(email)
                    .orElseGet(() -> createResearcher(currentIndex, email));
            researchers.add(researcher);
        }

        return researchers;
    }

    private Researcher createResearcher(int index, String email) {
        Researcher researcher = researcherFactory.create(
                "Pesquisador " + index,
                email,
                "senha123",
                "Universidade " + index
        );

        userGateway.save(researcher);
        return researcher;
    }

    private Event seedEvent() {
        Event event = new Event(
                "SBSI 2026",
                "Recife",
                LocalDate.now(),
                LocalDate.now().plusDays(4),
                LocalDate.now().plusDays(30),
                EventCategory.FULL_PAPER
        );

        eventManager.setCurrentEvent(event);
        return event;
    }

    private void seedThematicAreas(Event event) {
        List<String> areas = List.of(
                "Engenharia de Software",
                "Sistemas de Informacao",
                "Inteligencia Artificial",
                "Interacao Humano-Computador",
                "Banco de Dados"
        );

        for (String area : areas) {
            event.addThematicArea(new ThematicArea(area));
        }
    }

    private void seedCommittee(Event event, List<Researcher> reviewers) {
        List<ThematicArea> areas = event.getThematicAreas();

        for (int index = 0; index < reviewers.size(); index++) {
            Researcher reviewer = reviewers.get(index);
            reviewer.addThematicArea(areas.get(index));
            reviewer.addThematicArea(areas.get((index + 1) % areas.size()));
            event.addReviewer(reviewer);
        }
    }

    private List<Paper> seedPapers(Event event, List<Researcher> authors) {
        List<Paper> papers = new ArrayList<>();
        List<ThematicArea> areas = event.getThematicAreas();
        List<String> titles = List.of(
                "Uso de Padroes de Projeto em Sistemas Academicos",
                "Avaliacao de Usabilidade em Plataformas Digitais",
                "Mineracao de Dados para Apoio a Decisao",
                "Arquiteturas Limpas em Aplicacoes Java",
                "Analise de Qualidade em Software Livre",
                "Modelos Preditivos para Gestao Universitaria",
                "Experiencias de Ensino de Engenharia de Software",
                "Privacidade em Sistemas de Informacao",
                "Recomendacao de Revisores por Areas Tematicas",
                "Observabilidade em Sistemas de Submissao"
        );

        for (int index = 0; index < titles.size(); index++) {
            Researcher author = authors.get(index % authors.size());
            Paper paper = new Paper(
                    titles.get(index),
                    "Resumo demonstrativo do artigo " + (index + 1) + " para popular o sistema com dados de teste.",
                    author,
                    event
            );

            paper.addThematicArea(areas.get(index % areas.size()));
            paper.addThematicArea(areas.get((index + 1) % areas.size()));
            paper.addCollaborator(authors.get((index + 1) % authors.size()));

            papers.add(paperGateway.save(paper));
        }

        return papers;
    }

    private List<ReviewAssignment> seedAssignments(List<Paper> papers, List<Researcher> reviewers) {
        List<ReviewAssignment> assignments = new ArrayList<>();

        for (int index = 0; index < papers.size(); index++) {
            Paper paper = papers.get(index);
            paper.markAsUnderReview();
            paperGateway.save(paper);

            assignments.add(assignmentGateway.save(new ReviewAssignment(paper, reviewers.get(index % reviewers.size()))));
            assignments.add(assignmentGateway.save(new ReviewAssignment(paper, reviewers.get((index + 1) % reviewers.size()))));
        }

        return assignments;
    }

    private void seedReviews(List<ReviewAssignment> assignments) {
        int finishedAssignments = Math.min(16, assignments.size());

        for (int index = 0; index < finishedAssignments; index++) {
            ReviewAssignment assignment = assignments.get(index);
            ReviewVerdict verdict = verdictFor(index);

            assignment.finish(new Review(
                    assignment.getReviewer(),
                    "O artigo apresenta contribuicoes relevantes e uma proposta clara para o tema avaliado.",
                    "O texto pode melhorar a discussao dos trabalhos relacionados e detalhar melhor a metodologia.",
                    verdict
            ));

            assignmentGateway.save(assignment);

            if (index % 2 == 1) {
                closePaperDecision(assignments.get(index - 1), assignment);
            }
        }
    }

    private ReviewVerdict verdictFor(int index) {
        return switch ((index / 2) % 4) {
            case 0 -> ReviewVerdict.ACCEPTED;
            case 1 -> ReviewVerdict.WEAK_ACCEPTED;
            case 2 -> ReviewVerdict.WEAK_REJECTED;
            default -> ReviewVerdict.REJECTED;
        };
    }

    private void closePaperDecision(ReviewAssignment first, ReviewAssignment second) {
        Paper paper = second.getPaper();
        boolean accepted = first.getReview().getVerdict().isPositive()
                && second.getReview().getVerdict().isPositive();

        if (accepted) {
            paper.markAsAccepted();
        } else {
            paper.markAsRejected();
        }

        paperGateway.save(paper);
    }
}
