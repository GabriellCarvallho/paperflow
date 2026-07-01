package com.system.paperflow.application.dashboard;

import java.util.ArrayList;
import java.util.List;

public class SystemEventPublisher implements DashboardSubject {

    private final List<DashboardObserver> observers = new ArrayList<>();

    @Override
    public void subscribe(DashboardObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(DashboardObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (DashboardObserver observer : observers) {
            observer.update();
        }
    }
}