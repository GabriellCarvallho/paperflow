package com.system.paperflow.application.usecase.user;

import com.system.paperflow.application.exception.InvalidUserDataException;
import com.system.paperflow.application.factory.CoordinatorCreator;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.Coordinator;
import com.system.paperflow.domain.entity.User;

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

    public User execute() {
        Optional<User> existingUser = userPersistence.findByEmail(DEFAULT_EMAIL);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (!(user instanceof Coordinator)) {
                throw new InvalidUserDataException(
                        "O email do coordenador padrao ja esta cadastrado, mas nao pertence a um coordenador."
                );
            }

            return user;
        }

        User coordinator = coordinatorCreator.create(
                DEFAULT_USERNAME,
                DEFAULT_EMAIL,
                DEFAULT_PASSWORD,
                DEFAULT_INSTITUTION
        );

        userPersistence.save(coordinator);
        return coordinator;
    }
}