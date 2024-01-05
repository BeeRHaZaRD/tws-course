package ru.ifmo.webservices;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import jakarta.xml.ws.Endpoint;
import ru.ifmo.webservices.dao.MovieDAO;
import ru.ifmo.webservices.service.MovieWebService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.ws.fault.SOAPFaultBuilder.disableCaptureStackTrace", "false");

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
            HttpContext context = server.createContext("/MovieService");
            context.setAuthenticator(new BasicAuthenticator("realm") {
                @Override
                public boolean checkCredentials(String username, String password) {
                    return username.equals("admin") && password.equals("admin");
                }
            });

            MovieDAO movieDAO = new MovieDAO();
            Endpoint endpoint = Endpoint.create(new MovieWebService(movieDAO));

            endpoint.publish(context);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
