package com.system.paperflow.presentation.console;

import com.system.paperflow.application.exception.InvalidUserDataException;
import com.system.paperflow.application.exception.UserAlreadyExistsException;
import com.system.paperflow.application.factory.ResearcherCreator;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.application.usecase.user.RegisterUserUseCase;
import com.system.paperflow.domain.entity.User;
import com.system.paperflow.infrastructure.sqlite.SQLiteUserAdapter;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RF02UserRegistrationDemo {

    private RF02UserRegistrationDemo() {
        // Classe utilitaria para demonstracao no Main.
    }

    public static void run() {
        System.out.println("=== RF02 - Cadastro de usuarios ===");
        System.out.println("Padroes usados: Factory Method + Adapter");
        System.out.println();

        Path databasePath = Path.of("data", "paperflow.db");

        UserPersistence userPersistence = new SQLiteUserAdapter(databasePath);

        DefaultCoordinatorDemo.run(userPersistence);
        RF03TopicCompositeDemo.run(userPersistence);

        RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(
                userPersistence,
                new ResearcherCreator()
        );

        System.out.println("Banco SQLite usado automaticamente em:");
        System.out.println(databasePath.toAbsolutePath());
        System.out.println("Nenhuma instalacao manual do SQLite e necessaria.");
        System.out.println();

        String executionId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String email = "pesquisador." + executionId + "@ifpb.edu.br";

        testSuccessfulRegistration(registerUserUseCase, email);
        testDuplicateEmail(registerUserUseCase, email);
        testFindRegisteredUser(userPersistence, email);
        testInvalidUserData(registerUserUseCase);

        System.out.println("RF02 finalizado para simulacao inicial.");
        System.out.println("Mais tarde, esses mesmos casos de uso podem ser chamados pelas telas Swing.");
    }

    private static void testSuccessfulRegistration(RegisterUserUseCase registerUserUseCase, String email) {
        System.out.println("[1] Cadastro de novo usuario");

        User user = registerUserUseCase.execute(email, "1234", "IFPB");

        System.out.println("Usuario cadastrado com sucesso.");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Instituicao: " + user.getInstitution());
        System.out.println("Tipo inicial: " + user.getClass().getSimpleName());
        System.out.println();
    }

    private static void testDuplicateEmail(RegisterUserUseCase registerUserUseCase, String email) {
        System.out.println("[2] Tentativa de cadastro com email repetido");

        try {
            registerUserUseCase.execute(email, "abcd", "IFPB");
            System.out.println("Erro no teste: o sistema permitiu email duplicado.");
        } catch (UserAlreadyExistsException exception) {
            System.out.println("Comportamento esperado: " + exception.getMessage());
        }

        System.out.println();
    }

    private static void testFindRegisteredUser(UserPersistence userPersistence, String email) {
        System.out.println("[3] Consulta do usuario cadastrado no SQLite");

        userPersistence.findByEmail(email).ifPresentOrElse(
                user -> {
                    System.out.println("Usuario encontrado no banco.");
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Email: " + user.getEmail());
                    System.out.println("Instituicao: " + user.getInstitution());
                    System.out.println("Tipo salvo: " + user.getClass().getSimpleName());
                },
                () -> System.out.println("Erro no teste: usuario nao encontrado.")
        );

        System.out.println();
    }

    private static void testInvalidUserData(RegisterUserUseCase registerUserUseCase) {
        System.out.println("[4] Tentativa de cadastro com dados invalidos");

        try {
            registerUserUseCase.execute("email-invalido", "1234", "IFPB");
            System.out.println("Erro no teste: o sistema permitiu email invalido.");
        } catch (InvalidUserDataException exception) {
            System.out.println("Comportamento esperado: " + exception.getMessage());
        }

        System.out.println();
    }
}