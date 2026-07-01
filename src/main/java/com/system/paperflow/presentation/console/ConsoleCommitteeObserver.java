package com.system.paperflow.presentation.console;

import com.system.paperflow.application.observer.committee.CommitteeInvitationEvent;
import com.system.paperflow.application.observer.committee.CommitteeInvitationObserver;

public class ConsoleCommitteeObserver implements CommitteeInvitationObserver {

    @Override
    public void update(CommitteeInvitationEvent event) {
        System.out.println("[Observer/Comite] " + event.getMessage());
    }
}
