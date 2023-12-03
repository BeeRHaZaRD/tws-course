package ru.ifmo.webservices;

import jakarta.xml.ws.Endpoint;

public class App {
    public static void main(String[] args) {
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");

        String url = "http://0.0.0.0:8080/MovieService";
        Endpoint.publish(url, new MovieWebService());
    }
}
