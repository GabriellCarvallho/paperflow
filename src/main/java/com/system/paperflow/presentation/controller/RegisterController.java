package com.system.paperflow.presentation.controller;

import com.system.paperflow.application.exception.InvalidUserDataException;
import com.system.paperflow.application.exception.UserAlreadyExistsException;
import com.system.paperflow.application.exception.UserPersistenceException;
import com.system.paperflow.application.usecase.user.RegisterUserUseCase;

public class RegisterController {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private final RegisterUserUseCase useCase;

    public RegisterController(RegisterUserUseCase useCase) {
        this.useCase = useCase;
    }

    public ControllerResult execute(String institution, String email, String password) {
        
        if (isBlank(institution)) {
            return ControllerResult.failure("Instituição é obrigatória.");
        }

        if (isBlank(email)) {
            return ControllerResult.failure("E-mail é obrigatório.");
        }

        if (!email.trim().matches(EMAIL_REGEX)) {
            return ControllerResult.failure("E-mail inválido.");
        }

        if (isBlank(password)) {
            return ControllerResult.failure("Senha é obrigatória.");
        }

        if (password.length() <= 3) {
            return ControllerResult.failure("Senha deve ter mais que 3 caracteres.");
        }

        try {
            useCase.execute(email, password, institution);
            return ControllerResult.success("Usuário registrado com sucesso.");
        } catch (InvalidUserDataException | UserAlreadyExistsException | UserPersistenceException exception) {
            return ControllerResult.failure(exception.getMessage());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
