package com.system.paperflow.domain.entity;

public record ThematicArea(
    String name
) {

    @Override
    public boolean equals(Object arg0) {
        return arg0 == this || (arg0 instanceof ThematicArea other && java.util.Objects.equals(name, other.name));
    }
}
