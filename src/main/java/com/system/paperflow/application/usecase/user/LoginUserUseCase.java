package com.system.paperflow.application.usecase.user;

import com.system.paperflow.application.gateway.UserGateway;
import com.system.paperflow.commons.StringUtils;
import com.system.paperflow.domain.entity.Researcher;

public class LoginUserUseCase {

    private final UserGateway userGateway;

    public LoginUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public Researcher execute(String email, String password) {
        Researcher researcher = userGateway.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        if (!researcher.getPassword().equals(password)) throw new RuntimeException("Credenciais invalidas");

        return researcher;
    }
}
