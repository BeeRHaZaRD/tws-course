package ru.ifmo.webservices;

import jakarta.jws.WebService;
import java.util.List;

@WebService(serviceName = "MovieWebService",
        endpointInterface = "ru.ifmo.webservices.MovieService",
        targetNamespace = "http://webservices.ifmo.ru")
public class MovieWebService implements MovieService {
    public List<Movie> getAllMovies() {
        MovieDAO movieDAO = new MovieDAO();
        return movieDAO.getAllMovies();
    }

    public List<Movie> getMoviesByParams(Movie movieRequest) {
        MovieDAO movieDAO = new MovieDAO();
        return movieDAO.getMoviesByParams(movieRequest);
    }
}
