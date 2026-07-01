package com.system.paperflow.presentation.console;

import com.system.paperflow.application.factory.CoordinatorCreator;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.application.usecase.user.EnsureDefaultCoordinatorUseCase;
import com.system.paperflow.domain.entity.User;

public class DefaultCoordinatorDemo {

    private DefaultCoordinatorDemo() {
        // Classe utilitaria para demonstracao no Main.
    }

    public static void run(UserPersistence userPersistence) {
        System.out.println("=== Inicializacao do coordenador padrao ===");

        boolean alreadyExists = userPersistence.existsByEmail(EnsureDefaultCoordinatorUseCase.DEFAULT_EMAIL);

        EnsureDefaultCoordinatorUseCase ensureDefaultCoordinatorUseCase =
                new EnsureDefaultCoordinatorUseCase(userPersistence, new CoordinatorCreator());

        User coordinator = ensureDefaultCoordinatorUseCase.execute();

        if (alreadyExists) {
            System.out.println("Coordenador padrao ja existia no banco.");
        } else {
            System.out.println("Coordenador padrao criado automaticamente.");
        }

        System.out.println("Email: " + coordinator.getEmail());
        System.out.println("Senha: " + EnsureDefaultCoordinatorUseCase.DEFAULT_PASSWORD);
        System.out.println("Instituicao: " + coordinator.getInstitution());
        System.out.println("Tipo: " + coordinator.getClass().getSimpleName());
        System.out.println();
    }
}