package com.system.paperflow.infrastructure.gateway;

import com.system.paperflow.application.gateway.AuditLogGateway;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileAuditLogGateway implements AuditLogGateway {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path logFile;

    public FileAuditLogGateway(Path logFile) {
        this.logFile = logFile;
    }

    @Override
    public void register(String description) {
        try {
            Path parent = logFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String line = "[" + LocalDateTime.now().format(DATE_FORMAT) + "] " + description + System.lineSeparator();
            Files.writeString(logFile, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException exception) {
            throw new IllegalStateException("Nao foi possivel registrar auditoria.", exception);
        }
    }
}
