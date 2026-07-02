package com.system.paperflow.application.usecase.user;

import com.system.paperflow.application.exception.InvalidCommitteeInvitationException;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.User;

public class FindUserByEmailUseCase {

    private final UserPersistence userPersistence;

    public FindUserByEmailUseCase(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    public User execute(String email) {
        String normalizedEmail = email.trim().toLowerCase();

        return userPersistence.findByEmail(normalizedEmail)
                .orElseThrow(() -> new InvalidCommitteeInvitationException(
                        "Usuario nao encontrado no cadastro: " + normalizedEmail
                ));
    }
}
