package ru.ifmo.webservices.utils;

import java.util.function.Predicate;

public class Constraint<V> {
    protected final String fieldName;
    protected final V fieldValue;
    protected final Predicate<V> validationRule;
    protected final String faultMessage;

    public Constraint(String fieldName, V fieldValue, Predicate<V> validationRule, String faultMessage) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.validationRule = validationRule;
        this.faultMessage = faultMessage;
    }
}
