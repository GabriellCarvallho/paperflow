package com.system.paperflow.application.command;

import com.system.paperflow.application.gateway.AuditLogGateway;

public class CommandExecutor {

    private final AuditLogGateway auditLogGateway;

    public CommandExecutor(AuditLogGateway auditLogGateway) {
        this.auditLogGateway = auditLogGateway;
    }

    public <T> T execute(AuditableCommand<T> command) {
        T result = command.execute();
        auditLogGateway.register(command.description());
        return result;
    }
}
