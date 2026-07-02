package com.system.paperflow.application.usecase.user;

import com.system.paperflow.application.exception.InvalidUserDataException;
import com.system.paperflow.application.factory.CoordinatorCreator;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.Researcher;

import java.util.Optional;

public class EnsureDefaultCoordinatorUseCase {

    public static final String DEFAULT_USERNAME = "Coordenador Geral";
    public static final String DEFAULT_EMAIL = "coordenador@paperflow.com";
    public static final String DEFAULT_PASSWORD = "admin123";
    public static final String DEFAULT_INSTITUTION = "PaperFlow";

    private final UserPersistence userPersistence;
    private final CoordinatorCreator coordinatorCreator;

    public EnsureDefaultCoordinatorUseCase(
            UserPersistence userPersistence,
            CoordinatorCreator coordinatorCreator
    ) {
        this.userPersistence = userPersistence;
        this.coordinatorCreator = coordinatorCreator;
    }

    public Researcher execute() {
        Optional<Researcher> existingUser = userPersistence.findByEmail(DEFAULT_EMAIL);

        if (existingUser.isPresent()) {
            Researcher researcher = existingUser.get();

            if (!researcher.isCoordinator()) {
                throw new InvalidUserDataException(
                        "O email do coordenador padrao ja esta cadastrado, mas nao pertence a um coordenador."
                );
            }

            return researcher;
        }

        Researcher coordinator = coordinatorCreator.create(
                DEFAULT_USERNAME,
                DEFAULT_EMAIL,
                DEFAULT_PASSWORD,
                DEFAULT_INSTITUTION
        );

        userPersistence.save(coordinator);
        return coordinator;
    }
}
