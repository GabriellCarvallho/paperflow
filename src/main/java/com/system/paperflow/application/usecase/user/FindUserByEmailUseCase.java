package com.system.paperflow.application.usecase.user;

import com.system.paperflow.application.gateway.UserGateway;
import com.system.paperflow.commons.StringUtils;
import com.system.paperflow.domain.entity.Researcher;

public class FindUserByEmailUseCase {

    private final UserGateway userGateway;

    public FindUserByEmailUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public Researcher execute(String email) {

        return userGateway.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Usuario nao encontrado no cadastro: " + email
                ));
    }
}
