package ru.ifmo.webservices;

import jakarta.jws.WebService;
import java.util.List;

@WebService(serviceName = "MovieWebService",
        endpointInterface = "ru.ifmo.webservices.MovieService",
        targetNamespace = "http://webservices.ifmo.ru")
public class MovieWebService implements MovieService {
    private final MovieDAO movieDAO;

    public MovieWebService(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    public List<Movie> getAllMovies() {
        return movieDAO.getAllMovies();
    }

    public List<Movie> getMoviesByParams(Movie movieRequest) {
        return movieDAO.getMoviesByParams(movieRequest);
    }

    public Long createMovie(Movie movie) {
        return movieDAO.createMovie(movie);
    }

    public String updateMovie(Long movieId, Movie movie) {
        Long resultMovieId = movieDAO.updateMovie(movieId, movie);
        return resultMovieId != null ? "OK" : "Fault";
    }

    public String deleteMovie(Long id) {
        Long resultMovieId = movieDAO.deleteMovie(id);
        return resultMovieId != null ? "OK" : "Fault";
    }
}
