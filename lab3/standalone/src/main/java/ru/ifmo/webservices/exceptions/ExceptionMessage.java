package ru.ifmo.webservices.exceptions;

public enum ExceptionMessage {
    MISSING_ARGUMENT("The argument is missing"),
    EMPTY_ARGUMENT("The argument is empty"),
    MISSING_ARGUMENT_FIELDS("The argument does not contain all required fields"),
    INVALID_ARGUMENT_FIELDS("The argument has incorrect fields"),
    NOT_EXISTING_ID("No entry with the specified ID"),
    INTERNAL_ERROR("Internal Server Error");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
