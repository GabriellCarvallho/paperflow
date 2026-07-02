package com.system.paperflow.application.gateway;

import java.util.Optional;

import com.system.paperflow.domain.entity.Researcher;

public interface UserGateway {

    boolean existsByEmail(String email);

    void save(Researcher researcher);

    Optional<Researcher> findByEmail(String email);
}
