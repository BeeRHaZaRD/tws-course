package ru.ifmo.webservices;

import ru.ifmo.webservices.generated.Movie;
import ru.ifmo.webservices.generated.MovieWebService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://localhost:8080/MovieService?wsdl");
//        URL url = new URL("http://localhost:8080/webapp/ws/movieService?wsdl");
        MovieWebService movieService = new MovieWebService(url);

        List<Movie> movies;
        Movie movieRequest;

        System.out.println("Все фильмы:");
        movies = movieService.getMovieWebServicePort().getAllMovies();
        printMovieTable(movies);

        System.out.println("Фильмы 1997 года:");
        movieRequest = new Movie();
        movieRequest.setReleaseDate(1997);
        movies = movieService.getMovieWebServicePort().getMoviesByParams(movieRequest);
        printMovieTable(movies);

        System.out.println("Французские фильмы длительностью 110 минут:");
        movieRequest = new Movie();
        movieRequest.setCountry("France");
        movieRequest.setDuration(110);
        movies = movieService.getMovieWebServicePort().getMoviesByParams(movieRequest);
        printMovieTable(movies);

        System.out.println("Британские фильмы 2000 года длительностью 104 минуты:");
        movieRequest = new Movie();
        movieRequest.setReleaseDate(2000);
        movieRequest.setCountry("UK");
        movieRequest.setDuration(104);
        movies = movieService.getMovieWebServicePort().getMoviesByParams(movieRequest);
        printMovieTable(movies);
    }

    private static void printMovieTable(List<Movie> movies) {
        System.out.printf("%-38s | %-7s | %-7s | %-8s%n", "NAME", "RELEASE", "COUNTRY", "DURATION");
        System.out.printf("---------------------------------------------------------------------%n");
        for (Movie movie : movies) {
            System.out.printf("%-38s | %-7s | %-7s | %-8s%n",
                movie.getName(), movie.getReleaseDate(), movie.getCountry(), movie.getDuration());
        }
        System.out.println("\n");
    }
}
