package com.system.paperflow.presentation.console;

import com.system.paperflow.application.factory.ResearcherCreator;
import com.system.paperflow.application.observer.committee.CommitteeAuditTrailObserver;
import com.system.paperflow.application.observer.committee.CommitteeInvitationPublisher;
import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.application.usecase.committee.AcceptCommitteeInvitationUseCase;
import com.system.paperflow.application.usecase.committee.InviteReviewerUseCase;
import com.system.paperflow.application.usecase.committee.ListCommitteeInvitationsUseCase;
import com.system.paperflow.application.usecase.committee.ListTechnicalCommitteeUseCase;
import com.system.paperflow.application.usecase.committee.RejectCommitteeInvitationUseCase;
import com.system.paperflow.application.usecase.user.EnsureDefaultCoordinatorUseCase;
import com.system.paperflow.application.usecase.user.RegisterUserUseCase;
import com.system.paperflow.domain.entity.CommitteeInvitation;
import com.system.paperflow.domain.entity.Reviewer;
import com.system.paperflow.domain.entity.Topic;
import com.system.paperflow.infrastructure.sqlite.SQLiteCommittee;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class RF04CommitteeDemo {

    private RF04CommitteeDemo() {
        // Classe utilitaria para demonstracao no Main.
    }

    public static void run(UserPersistence userPersistence) {
        System.out.println("=== RF04 - Comite tecnico de avaliacao ===");
        System.out.println("Padrao usado: Observer");
        System.out.println();

        Path databasePath = Path.of("data", "paperflow.db");
        CommitteePersistence committeePersistence = new SQLiteCommittee(databasePath);

        CommitteeInvitationPublisher publisher = new CommitteeInvitationPublisher();
        CommitteeAuditTrailObserver auditTrailObserver = new CommitteeAuditTrailObserver();

        publisher.subscribe(new ConsoleCommitteeObserver());
        publisher.subscribe(auditTrailObserver);

        InviteReviewerUseCase inviteReviewerUseCase = new InviteReviewerUseCase(
                userPersistence,
                committeePersistence,
                publisher
        );
        AcceptCommitteeInvitationUseCase acceptInvitationUseCase = new AcceptCommitteeInvitationUseCase(
                committeePersistence,
                publisher
        );
        RejectCommitteeInvitationUseCase rejectInvitationUseCase = new RejectCommitteeInvitationUseCase(
                committeePersistence,
                publisher
        );
        ListTechnicalCommitteeUseCase listTechnicalCommitteeUseCase = new ListTechnicalCommitteeUseCase(committeePersistence);
        ListCommitteeInvitationsUseCase listCommitteeInvitationsUseCase = new ListCommitteeInvitationsUseCase(committeePersistence);

        String executionId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String reviewerAcceptedEmail = "revisor.aceita." + executionId + "@ifpb.edu.br";
        String reviewerRejectedEmail = "revisor.recusa." + executionId + "@ifpb.edu.br";

        ensureResearcher(userPersistence, reviewerAcceptedEmail, "IFPB");
        ensureResearcher(userPersistence, reviewerRejectedEmail, "UFPB");

        CommitteeInvitation acceptedInvitation = inviteReviewerUseCase.execute(
                EnsureDefaultCoordinatorUseCase.DEFAULT_EMAIL,
                reviewerAcceptedEmail
        );
        CommitteeInvitation rejectedInvitation = inviteReviewerUseCase.execute(
                EnsureDefaultCoordinatorUseCase.DEFAULT_EMAIL,
                reviewerRejectedEmail
        );

        acceptInvitationUseCase.execute(
                acceptedInvitation.getId(),
                reviewerAcceptedEmail,
                Set.of(
                        new Topic("Machine Learning", "Expertise em aprendizado de maquina."),
                        new Topic("LLMs", "Expertise em grandes modelos de linguagem.")
                )
        );

        rejectInvitationUseCase.execute(rejectedInvitation.getId(), reviewerRejectedEmail);

        printInvitations(listCommitteeInvitationsUseCase.execute());
        printCommitteeMembers(listTechnicalCommitteeUseCase.execute());
        printAuditTrail(auditTrailObserver);

        System.out.println("RF04 finalizado para simulacao inicial.");
    }

    private static void ensureResearcher(UserPersistence userPersistence, String email, String institution) {
        if (userPersistence.existsByEmail(email)) {
            return;
        }

        RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(
                userPersistence,
                new ResearcherCreator()
        );

        registerUserUseCase.execute(email, "1234", institution);
    }

    private static void printInvitations(List<CommitteeInvitation> invitations) {
        System.out.println();
        System.out.println("Convites registrados:");

        for (CommitteeInvitation invitation : invitations) {
            System.out.println("- " + invitation.getReviewerEmail() + " | status: " + invitation.getStatus());
        }
    }

    private static void printCommitteeMembers(List<Reviewer> reviewers) {
        System.out.println();
        System.out.println("Revisores aceitos no comite tecnico:");

        for (Reviewer reviewer : reviewers) {
            System.out.println("- " + reviewer.getEmail());
            System.out.println("  Areas de expertise:");

            for (Topic topic : reviewer.getExpertiseAreas()) {
                System.out.println("  * " + topic.getName());
            }
        }
    }

    private static void printAuditTrail(CommitteeAuditTrailObserver auditTrailObserver) {
        System.out.println();
        System.out.println("Historico interno observado pelo Observer:");

        for (String record : auditTrailObserver.getRecords()) {
            System.out.println("- " + record);
        }

        System.out.println();
    }
}
