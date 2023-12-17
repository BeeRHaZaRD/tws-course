package ru.ifmo.webservices.exceptions;

import jakarta.xml.ws.WebFault;

import java.io.Serial;

@WebFault(faultBean = "ru.ifmo.webservices.exceptions.MovieServiceFault")
public class OperationException extends Exception {
    @Serial
    private static final long serialVersionUID = 2585947840375540478L;
    private final MovieServiceFault faultInfo;

    public OperationException(String message, MovieServiceFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public OperationException(String message, MovieServiceFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public MovieServiceFault getFaultInfo() {
        return faultInfo;
    }
}
