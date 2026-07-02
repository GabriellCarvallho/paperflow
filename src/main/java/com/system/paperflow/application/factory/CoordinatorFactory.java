package com.system.paperflow.application.factory;

import com.system.paperflow.domain.entity.CoordinatorProfile;
import com.system.paperflow.domain.entity.Researcher;

public class CoordinatorFactory {

    public Researcher create(String username, String email, String password, String institution) {
        Researcher researcher = new Researcher(username, email, password, institution);
        researcher.addProfile(new CoordinatorProfile());
        return researcher;
    }
}
