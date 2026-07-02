package com.system.paperflow.application.command;

import java.util.function.Supplier;

public class SimpleAuditableCommand<T> implements AuditableCommand<T> {

    private final Supplier<T> action;
    private final String description;

    public SimpleAuditableCommand(Supplier<T> action, String description) {
        this.action = action;
        this.description = description;
    }

    @Override
    public T execute() {
        return action.get();
    }

    @Override
    public String description() {
        return description;
    }
}
