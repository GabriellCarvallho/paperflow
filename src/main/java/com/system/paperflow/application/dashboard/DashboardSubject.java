package com.system.paperflow.application.dashboard;

public interface DashboardSubject {
    void subscribe(DashboardObserver observer);
    void unsubscribe(DashboardObserver observer);
    void notifyObservers();
}