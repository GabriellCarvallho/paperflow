package com.system.paperflow.application.usecase.user;

import com.system.paperflow.application.factory.CoordinatorFactory;
import com.system.paperflow.application.gateway.UserGateway;
import com.system.paperflow.domain.entity.Researcher;

import java.util.Optional;

public class EnsureDefaultCoordinatorUseCase {

    public static final String DEFAULT_USERNAME = "Coordenador Geral";
    public static final String DEFAULT_EMAIL = "coordenador@paperflow.com";
    public static final String DEFAULT_PASSWORD = "admin123";
    public static final String DEFAULT_INSTITUTION = "PaperFlow";

    private final UserGateway userGateway;
    private final CoordinatorFactory coordinatorFactory;

    public EnsureDefaultCoordinatorUseCase(
            UserGateway userGateway,
            CoordinatorFactory coordinatorFactory
    ) {
        this.userGateway = userGateway;
        this.coordinatorFactory = coordinatorFactory;
    }

    public Researcher execute() {
        Optional<Researcher> existingUser = userGateway.findByEmail(DEFAULT_EMAIL);

        if (existingUser.isPresent()) {
            Researcher researcher = existingUser.get();

            if (!researcher.isCoordinator()) {
                throw new RuntimeException(
                        "O email do coordenador padrao ja esta cadastrado, mas nao pertence a um coordenador."
                );
            }

            return researcher;
        }

        Researcher coordinator = coordinatorFactory.create(
                DEFAULT_USERNAME,
                DEFAULT_EMAIL,
                DEFAULT_PASSWORD,
                DEFAULT_INSTITUTION
        );

        userGateway.save(coordinator);
        return coordinator;
    }
}
