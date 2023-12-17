package ru.ifmo.webservices;

import jakarta.xml.ws.Endpoint;
import ru.ifmo.webservices.dao.MovieDAO;
import ru.ifmo.webservices.service.MovieWebService;

public class App {
    public static void main(String[] args) {
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.ws.fault.SOAPFaultBuilder.disableCaptureStackTrace", "false");

        MovieDAO movieDAO = new MovieDAO();

        Endpoint.publish("http://0.0.0.0:8080/MovieService", new MovieWebService(movieDAO));
    }
}
