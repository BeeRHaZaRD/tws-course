package ru.ifmo.webservices;

import jakarta.xml.ws.Endpoint;

public class App {
    public static void main(String[] args) {
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");

        MovieDAO movieDAO = new MovieDAO();

        Endpoint.publish("http://0.0.0.0:8080/MovieService", new MovieWebService(movieDAO));
    }
}
