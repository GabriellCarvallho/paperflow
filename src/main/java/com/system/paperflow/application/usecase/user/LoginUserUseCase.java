package com.system.paperflow.application.usecase.user;

import com.system.paperflow.application.exception.InvalidCredentialsException;
import com.system.paperflow.application.exception.UserPersistenceException;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.Researcher;

public class LoginUserUseCase {

    private final UserPersistence userPersistence;

    public LoginUserUseCase(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    public Researcher execute(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();
        Researcher researcher = userPersistence.findByEmail(normalizedEmail).orElseThrow(() -> new UserPersistenceException("Usuario não encontrado"));

        if (!researcher.getPassword().equals(password)) throw new InvalidCredentialsException();

        return researcher;
    }
}
