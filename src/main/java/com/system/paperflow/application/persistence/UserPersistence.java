package com.system.paperflow.application.persistence;

import java.util.Optional;

import com.system.paperflow.domain.entity.Researcher;

public interface UserPersistence {

    boolean existsByEmail(String email);

    void save(Researcher researcher);

    Optional<Researcher> findByEmail(String email);
}
