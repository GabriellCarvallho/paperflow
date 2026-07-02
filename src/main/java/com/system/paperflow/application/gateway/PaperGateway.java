package com.system.paperflow.application.gateway;

import java.util.List;
import java.util.Optional;

import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.ReviewAssignment;

public interface PaperGateway {

    Paper save(Paper paper);

    Optional<Paper> findById(String id);

    List<Paper> findByEvent(Event event);

    List<Paper> findByAuthorEmail(String authorEmail);

}
