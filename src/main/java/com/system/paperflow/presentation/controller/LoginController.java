package com.system.paperflow.presentation.controller;

import com.system.paperflow.application.exception.InvalidCredentialsException;
import com.system.paperflow.application.exception.UserPersistenceException;
import com.system.paperflow.application.usecase.user.LoginUserUseCase;

public class LoginController {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private final LoginUserUseCase useCase;

    public LoginController(LoginUserUseCase useCase) {
        this.useCase = useCase;
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
            useCase.execute(email, password);
            return ControllerResult.success("Login realizado com sucesso.");
        } catch (InvalidCredentialsException | UserPersistenceException exception) {
            return ControllerResult.failure(exception.getMessage());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
