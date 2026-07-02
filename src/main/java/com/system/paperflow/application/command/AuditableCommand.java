package com.system.paperflow.application.command;

public interface AuditableCommand<T> {

    T execute();

    String description();
}
