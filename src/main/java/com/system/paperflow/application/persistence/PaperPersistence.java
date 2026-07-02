package com.system.paperflow.application.persistence;

import com.system.paperflow.domain.entity.Paper;
import java.util.List;
import java.util.Optional;

public interface PaperPersistence {

    void save(Paper paper);

    Optional<Paper> findById(String id);

    List<Paper> findAll();
}
