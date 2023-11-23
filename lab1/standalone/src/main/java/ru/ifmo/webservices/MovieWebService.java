package ru.ifmo.webservices;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import java.util.List;
import java.util.Map;

@WebService(serviceName = "MovieService")
public class MovieWebService {
    @WebMethod(operationName = "getAllMovies")
    public List<Movie> getAllMovies() {
        MovieDAO movieDAO = new MovieDAO();
        return movieDAO.getAllMovies();
    }

    @WebMethod(operationName = "getMoviesByParams")
    public List<Movie> getMoviesByParams(Map<String, Object> paramsMap) {
        MovieDAO movieDAO = new MovieDAO();
        return movieDAO.getMoviesByParams(paramsMap);
    }
}
