package ru.ifmo.webservices;

import ru.ifmo.webservices.generated.GetMoviesByParams;
import ru.ifmo.webservices.generated.Movie;
import ru.ifmo.webservices.generated.MovieService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://localhost:8080/MovieService?wsdl");
//        URL url = new URL("http://localhost:8080/webapp/ws/movieService?wsdl");
        MovieService movieService = new MovieService(url);

        List<Movie> movies;
        Map<String, Object> paramsMap;

        System.out.println("Все фильмы:");
        movies = movieService.getMovieWebServicePort().getAllMovies();
        printMovieTable(movies);

        System.out.println("Фильмы 1997 года:");
        paramsMap = new HashMap<>();
        paramsMap.put("release_date", 1997);
        movies = movieService.getMovieWebServicePort().getMoviesByParams(createRequestParams(paramsMap));
        printMovieTable(movies);

        System.out.println("Французские фильмы длительностью 110 минут:");
        paramsMap = new HashMap<>();
        paramsMap.put("country", "France");
        paramsMap.put("duration", 110);
        movies = movieService.getMovieWebServicePort().getMoviesByParams(createRequestParams(paramsMap));
        printMovieTable(movies);

        System.out.println("Британские фильмы 2000 года длительностью 104 минуты:");
        paramsMap = new HashMap<>();
        paramsMap.put("release_date", 2000);
        paramsMap.put("country", "UK");
        paramsMap.put("duration", 104);
        movies = movieService.getMovieWebServicePort().getMoviesByParams(createRequestParams(paramsMap));
        printMovieTable(movies);
    }

    private static GetMoviesByParams.Arg0 createRequestParams(Map<String, Object> paramsMap) {
        var params = new GetMoviesByParams.Arg0();
        for (String paramKey : paramsMap.keySet()) {
            var nameEntry = new GetMoviesByParams.Arg0.Entry();
            nameEntry.setKey(paramKey);
            nameEntry.setValue(paramsMap.get(paramKey));
            params.getEntry().add(nameEntry);
        }
        return params;
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
