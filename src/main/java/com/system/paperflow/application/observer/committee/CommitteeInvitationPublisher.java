package com.system.paperflow.application.observer.committee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommitteeInvitationPublisher {

    private final List<CommitteeInvitationObserver> observers = new ArrayList<>();

    public void subscribe(CommitteeInvitationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unsubscribe(CommitteeInvitationObserver observer) {
        observers.remove(observer);
    }

    public List<CommitteeInvitationObserver> getObservers() {
        return Collections.unmodifiableList(observers);
    }

    public void notifyObservers(CommitteeInvitationEvent event) {
        for (CommitteeInvitationObserver observer : observers) {
            observer.update(event);
        }
    }
}
