package ru.ifmo.webservices.service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import ru.ifmo.webservices.exceptions.OperationException;
import ru.ifmo.webservices.model.Movie;

import java.util.List;

@WebService(name = "movieService", targetNamespace = "http://webservices.ifmo.ru")
public interface MovieService {
    @WebMethod(operationName = "getAllMovies")
    List<Movie> getAllMovies() throws OperationException;

    @WebMethod(operationName = "getMoviesByParams")
    List<Movie> getMoviesByParams(@WebParam(name="movieRequest") Movie movieRequest) throws OperationException;

    @WebMethod(operationName = "createMovie")
    Long createMovie(@WebParam(name="movie") Movie movie) throws OperationException;

    @WebMethod(operationName = "updateMovie")
    String updateMovie(@WebParam(name="movieId") Long movieId, @WebParam(name="movie") Movie movie) throws OperationException;

    @WebMethod(operationName = "deleteMovie")
    String deleteMovie(@WebParam(name="movieId") Long movieId) throws OperationException;
}
