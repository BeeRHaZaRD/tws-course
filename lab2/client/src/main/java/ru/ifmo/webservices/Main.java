package ru.ifmo.webservices;

import ru.ifmo.webservices.generated.Movie;
import ru.ifmo.webservices.generated.MovieService;
import ru.ifmo.webservices.generated.MovieWebService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://localhost:8080/MovieService?wsdl");
//        URL url = new URL("http://localhost:8080/webapp/ws/movieService?wsdl");
        MovieWebService movieWebService = new MovieWebService(url);
        MovieService movieService = movieWebService.getMovieWebServicePort();

        List<Movie> resultMovies;
        Movie movie;
        Long movieId;
        String responseStatus;

        movie = new Movie();
        movie.setName("Back to the Future");
        movie.setReleaseDate(1985);
        movie.setCountry("USA");
        movie.setDuration(116);
        movieId = movieService.createMovie(movie);
        System.out.printf("Добавлен фильм с id=%d%n%n", movieId);

        System.out.println("Все фильмы:");
        resultMovies = movieService.getAllMovies();
        printMovieTable(resultMovies);

        movie = new Movie();
        movieId = 12L;
        movie.setCountry("Canada");
        responseStatus = movieService.updateMovie(movieId, movie);
        System.out.println("Обновление сущности с id=" + movieId + ": " + responseStatus + "\n");

        System.out.println("Все фильмы:");
        resultMovies = movieService.getAllMovies();
        printMovieTable(resultMovies);

//        movieId = 16L;
//        responseStatus = movieService.deleteMovie(movieId);
//        System.out.println("Удаление сущности с id=" + movieId + ": " + responseStatus + "\n");
//
//        System.out.println("Все фильмы:");
//        resultMovies = movieService.getAllMovies();
//        printMovieTable(resultMovies);
    }

    private static void printMovieTable(List<Movie> movies) {
        final String printFormat = "%3s | %-38s | %-7s | %-7s | %-8s%n";
        System.out.printf(printFormat, "ID", "NAME", "RELEASE", "COUNTRY", "DURATION");
        System.out.println("-".repeat(75));
        for (Movie movie : movies) {
            System.out.printf(printFormat,
                movie.getId(), movie.getName(), movie.getReleaseDate(), movie.getCountry(), movie.getDuration());
        }
        System.out.println("\n");
    }
}
