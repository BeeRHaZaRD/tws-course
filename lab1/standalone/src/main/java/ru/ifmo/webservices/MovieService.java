package ru.ifmo.webservices;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.List;

@WebService(name = "movieService", targetNamespace = "http://webservices.ifmo.ru")
public interface MovieService {
    @WebMethod(operationName = "getAllMovies")
    List<Movie> getAllMovies();

    @WebMethod(operationName = "getMoviesByParams")
    List<Movie> getMoviesByParams(@WebParam(name="movieRequest") Movie movieRequest);
}
