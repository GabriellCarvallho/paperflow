package com.system.paperflow.domain.entity;

public enum ReviewVerdict {

    REJECTED,
    WEAK_REJECTED,
    WEAK_ACCEPTED,
    ACCEPTED;

    public boolean isPositive() {
        return this == ACCEPTED || this == WEAK_ACCEPTED;
    }

}
