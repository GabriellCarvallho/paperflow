package com.system.paperflow.domain.state;
import com.system.paperflow.domain.entity.Paper;

public interface PaperState {
    String getStatus();
    void advance(Paper pape);

}
