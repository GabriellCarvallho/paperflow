package com.system.paperflow.infrastructure.gateway;

import com.system.paperflow.application.gateway.UserGateway;
import com.system.paperflow.domain.entity.Researcher;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserGateway implements UserGateway {

    private final Map<String, Researcher> researchersByEmail = new LinkedHashMap<>();

    @Override
    public boolean existsByEmail(String email) {
        return researchersByEmail.containsKey(normalize(email));
    }

    @Override
    public void save(Researcher researcher) {
        researchersByEmail.put(normalize(researcher.getEmail()), researcher);
    }

    @Override
    public Optional<Researcher> findByEmail(String email) {
        return Optional.ofNullable(researchersByEmail.get(normalize(email)));
    }

    private String normalize(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
