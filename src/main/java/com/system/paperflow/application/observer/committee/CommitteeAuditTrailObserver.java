package com.system.paperflow.application.observer.committee;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommitteeAuditTrailObserver implements CommitteeInvitationObserver {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final List<String> records = new ArrayList<>();

    @Override
    public void update(CommitteeInvitationEvent event) {
        String record = "[" + event.getOccurredAt().format(FORMATTER) + "] "
                + event.getType()
                + " - "
                + event.getMessage();

        records.add(record);
    }

    public List<String> getRecords() {
        return Collections.unmodifiableList(records);
    }
}
