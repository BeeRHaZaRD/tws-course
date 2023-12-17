package ru.ifmo.webservices.service;

import jakarta.jws.WebService;
import ru.ifmo.webservices.dao.MovieDAO;
import ru.ifmo.webservices.exceptions.OperationException;
import ru.ifmo.webservices.model.Movie;
import ru.ifmo.webservices.utils.AbstractValidator;
import ru.ifmo.webservices.validators.RequestType;
import ru.ifmo.webservices.validators.MovieValidator;

import java.util.List;

@WebService(serviceName = "MovieWebService",
        endpointInterface = "ru.ifmo.webservices.service.MovieService",
        targetNamespace = "http://webservices.ifmo.ru")
public class MovieWebService implements MovieService {
    private final MovieDAO movieDAO;

    public MovieWebService(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    public List<Movie> getAllMovies() throws OperationException {
        return movieDAO.getAllMovies();
    }

    public List<Movie> getMoviesByParams(Movie movieRequest) throws OperationException {
        AbstractValidator.checkPresence(movieRequest, "movieRequest");

        MovieValidator movieValidator = new MovieValidator(movieRequest, true, RequestType.PARAMS);
        movieValidator.validate();

        return movieDAO.getMoviesByParams(movieRequest);
    }

    public Long createMovie(Movie movie) throws OperationException {
        AbstractValidator.checkPresence(movie, "movie");

        MovieValidator movieValidator = new MovieValidator(movie, true, RequestType.CREATE);
        movieValidator.validate();

        return movieDAO.createMovie(movie);
    }

    public String updateMovie(Long movieId, Movie movie) throws OperationException {
        AbstractValidator.checkPresence(movieId, "movieId");
        AbstractValidator.checkPresence(movie, "movie");

        MovieValidator movieValidator = new MovieValidator(movie, true, RequestType.UPDATE);
        movieValidator.validate();

        Long resultMovieId = movieDAO.updateMovie(movieId, movie);
        return resultMovieId != null ? "OK" : "Fault";
    }

    public String deleteMovie(Long movieId) throws OperationException {
        AbstractValidator.checkPresence(movieId, "movieId");

        Long resultMovieId = movieDAO.deleteMovie(movieId);
        return resultMovieId != null ? "OK" : "Fault";
    }
}
