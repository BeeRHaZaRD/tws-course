package ru.ifmo.webservices.exceptions;

import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "MovieServiceFault", propOrder = {"message"})
public class MovieServiceFault {
    protected String message;

    public MovieServiceFault(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
