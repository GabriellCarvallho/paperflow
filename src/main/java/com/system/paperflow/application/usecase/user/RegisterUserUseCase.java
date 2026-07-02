package com.system.paperflow.application.usecase.user;

import com.system.paperflow.application.exception.UserAlreadyExistsException;
import com.system.paperflow.application.factory.ResearcherCreator;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.Researcher;

public class RegisterUserUseCase {

    private final UserPersistence userPersistence;
    private final ResearcherCreator researcherCreator;

    public RegisterUserUseCase(UserPersistence userPersistence, ResearcherCreator researcherCreator) {
        this.userPersistence = userPersistence;
        this.researcherCreator = researcherCreator;
    }

    public Researcher execute(String email, String password, String institution) {
        String normalizedEmail = email.trim().toLowerCase();

        if (userPersistence.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException(normalizedEmail);
        }

        String username = createUsernameFromEmail(normalizedEmail);
        Researcher researcher = researcherCreator.create(username, normalizedEmail, password, institution.trim());
        userPersistence.save(researcher);

        return researcher;
    }

    private String createUsernameFromEmail(String email) {
        int atIndex = email.indexOf('@');
        return atIndex > 0 ? email.substring(0, atIndex) : email;
    }
}
