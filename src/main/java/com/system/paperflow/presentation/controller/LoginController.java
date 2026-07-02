package com.system.paperflow.presentation.controller;

import java.util.Optional;

import com.system.paperflow.application.exception.UserPersistenceException;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.User;

public class LoginController {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private final UserPersistence userPersistence;

    public LoginController(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    public ControllerResult execute(String email, String password) {
        if (isBlank(email)) {
            return ControllerResult.failure("E-mail é obrigatório.");
        }

        if (!email.trim().matches(EMAIL_REGEX)) {
            return ControllerResult.failure("E-mail inválido.");
        }

        if (isBlank(password)) {
            return ControllerResult.failure("Senha é obrigatória.");
        }

        try {
            Optional<User> user = userPersistence.findByEmail(email);

            if (user.isEmpty() || !user.get().getPassword().equals(password)) {
                return ControllerResult.failure("E-mail ou senha inválidos.");
            }

            return ControllerResult.success("Login realizado com sucesso.");
        } catch (UserPersistenceException exception) {
            return ControllerResult.failure(exception.getMessage());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
