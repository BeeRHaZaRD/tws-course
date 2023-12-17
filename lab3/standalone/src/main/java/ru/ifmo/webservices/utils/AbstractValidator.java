package ru.ifmo.webservices.utils;

import ru.ifmo.webservices.exceptions.ExceptionMessage;
import ru.ifmo.webservices.exceptions.OperationException;
import ru.ifmo.webservices.exceptions.MovieServiceFault;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

public class AbstractValidator {
    private final boolean notEmptyRequired;
    private boolean isEmpty = true;
    private final List<String> missingFields = new LinkedList<>();
    private final Map<String, String> invalidFieldMessages = new HashMap<>();

    protected AbstractValidator(boolean notEmptyRequired) {
        this.notEmptyRequired = notEmptyRequired;
    }

    public static void checkPresence(Object value, String argName) throws OperationException {
        if (value == null) {
            throw new OperationException(
                    ExceptionMessage.MISSING_ARGUMENT.message(),
                    new MovieServiceFault("The " + argName + " argument is missing"));
        }
    }

    protected <V> void addConstraint(Constraint<V> constraint, boolean isRequired) {
        // для проверки на наличие хотя бы 1 поля
        if (constraint.fieldValue != null && this.isEmpty) {
            this.isEmpty = false;
        }

        if (isRequired && constraint.fieldValue == null) {
            this.missingFields.add(constraint.fieldName);
            return;
        }

        if (constraint.fieldValue != null && !constraint.validationRule.test(constraint.fieldValue)) {
            this.invalidFieldMessages.put(constraint.fieldName, constraint.faultMessage);
        }
    }

    public void validate() throws OperationException {
        if (this.notEmptyRequired && this.isEmpty) {
            throw new OperationException(
                    ExceptionMessage.EMPTY_ARGUMENT.message(),
                    new MovieServiceFault("At least one field must be specified"));
        }
        this.checkMissingFields();
        this.checkInvalidFields();
    }

    private void checkMissingFields() throws OperationException {
        if (this.missingFields.isEmpty()) {
            return;
        }

        StringBuilder faultMessageBuilder = new StringBuilder("These fields are missing:\n");
        for (String missingField : this.missingFields) {
            faultMessageBuilder
                    .append(" - ")
                    .append(missingField)
                    .append("\n");
        }
        throw new OperationException(
                ExceptionMessage.MISSING_ARGUMENT_FIELDS.message(),
                new MovieServiceFault(faultMessageBuilder.toString()));
    }

    private void checkInvalidFields() throws OperationException {
        if (this.invalidFieldMessages.isEmpty()) {
            return;
        }

        StringBuilder constraintsBuilder = new StringBuilder();
        for (String field : this.invalidFieldMessages.keySet()) {
            constraintsBuilder
                    .append(" - ")
                    .append(field)
                    .append(" must be ")
                    .append(this.invalidFieldMessages.get(field))
                    .append("\n");
        }
        throw new OperationException(
                ExceptionMessage.INVALID_ARGUMENT_FIELDS.message(),
                new MovieServiceFault(constraintsBuilder.toString()));
    }
}
