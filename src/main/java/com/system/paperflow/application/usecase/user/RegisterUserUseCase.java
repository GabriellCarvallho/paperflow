package com.system.paperflow.application.usecase.user;

import com.system.paperflow.application.exception.InvalidUserDataException;
import com.system.paperflow.application.exception.UserAlreadyExistsException;
import com.system.paperflow.application.factory.UserCreator;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.User;

public class RegisterUserUseCase {

    private final UserPersistence userPersistence;
    private final UserCreator userCreator;

    public RegisterUserUseCase(UserPersistence userPersistence, UserCreator userCreator) {
        this.userPersistence = userPersistence;
        this.userCreator = userCreator;
    }

    public User execute(String email, String password, String institution) {
        validate(email, password, institution);

        String normalizedEmail = email.trim().toLowerCase();

        if (userPersistence.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException(normalizedEmail);
        }

        String username = createUsernameFromEmail(normalizedEmail);
        User user = userCreator.create(username, normalizedEmail, password, institution.trim());
        userPersistence.save(user);

        return user;
    }

    private void validate(String email, String password, String institution) {
        if (isBlank(email)) {
            throw new InvalidUserDataException("Email is required.");
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new InvalidUserDataException("Email must be valid.");
        }

        if (isBlank(password)) {
            throw new InvalidUserDataException("Password is required.");
        }

        if (password.length() < 4) {
            throw new InvalidUserDataException("Password must have at least 4 characters.");
        }

        if (isBlank(institution)) {
            throw new InvalidUserDataException("Institution is required.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String createUsernameFromEmail(String email) {
        int atIndex = email.indexOf('@');
        return atIndex > 0 ? email.substring(0, atIndex) : email;
    }
}
