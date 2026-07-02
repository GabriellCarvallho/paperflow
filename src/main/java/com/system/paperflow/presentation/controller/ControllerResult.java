package com.system.paperflow.presentation.controller;

public record ControllerResult(boolean success, String message) {

    public static ControllerResult success(String message) {
        return new ControllerResult(true, message);
    }

    public static ControllerResult failure(String message) {
        return new ControllerResult(false, message);
    }
}
