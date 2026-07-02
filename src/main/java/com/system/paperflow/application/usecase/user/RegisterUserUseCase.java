package com.system.paperflow.application.usecase.user;

import com.system.paperflow.application.factory.ResearcherFactory;
import com.system.paperflow.application.gateway.UserGateway;
import com.system.paperflow.commons.StringUtils;
import com.system.paperflow.domain.entity.Researcher;

public class RegisterUserUseCase {

    private final UserGateway userGateway;
    private final ResearcherFactory researcherFactory;

    public RegisterUserUseCase(UserGateway userGateway, ResearcherFactory researcherFactory) {
        this.userGateway = userGateway;
        this.researcherFactory = researcherFactory;
    }

    public Researcher execute(String email, String password, String institution) {

        if (userGateway.existsByEmail(email)) {
            throw new RuntimeException("Usuario ja cadastrado com o email: " + email);
        }

        String username = createUsernameFromEmail(email);
        Researcher researcher = researcherFactory.create(username, email, password, institution);
        userGateway.save(researcher);

        return researcher;
    }

    private String createUsernameFromEmail(String email) {
        int atIndex = email.indexOf('@');
        return atIndex > 0 ? email.substring(0, atIndex) : email;
    }
}
