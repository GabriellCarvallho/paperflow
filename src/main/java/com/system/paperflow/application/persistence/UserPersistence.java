package com.system.paperflow.application.persistence;

import java.util.Optional;

import com.system.paperflow.domain.entity.User;

public interface UserPersistence {

    boolean existsByEmail(String email);

    void save(User user);

    Optional<User> findByEmail(String email);
}
