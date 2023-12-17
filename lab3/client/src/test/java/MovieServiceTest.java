import org.junit.jupiter.api.*;
import ru.ifmo.webservices.generated.Movie;
import ru.ifmo.webservices.generated.MovieService;
import ru.ifmo.webservices.generated.MovieWebService;
import ru.ifmo.webservices.generated.OperationException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MovieServiceTest {
    private MovieService movieService;

    @BeforeAll
    public void setup() throws MalformedURLException {
        URL url = new URL("http://localhost:8080/MovieService?wsdl");
        MovieWebService movieWebService = new MovieWebService(url);
        this.movieService = movieWebService.getMovieWebServicePort();
    }

    @Test
    @Order(1)
    @DisplayName("getMoviesByParams: корректный результат при корректных параметрах запроса")
    public void getMoviesByParams_ValidArgument_NotEmptyList() throws OperationException {
        Movie movieParams = new Movie();
        movieParams.setCountry("France");
        movieParams.setDuration(110);

        List<Movie> resultMovies = movieService.getMoviesByParams(movieParams);
        List<MovieMapped> resultMoviesMapped = resultMovies.stream().map(MovieMapped::ofMovie).toList();

        List<MovieMapped> expectedMovies = new LinkedList<>();
        expectedMovies.add(new MovieMapped("Leon", 1994, "France", 110));
        expectedMovies.add(new MovieMapped("Billy Elliot", 2000, "France", 110));

        Assertions.assertEquals(expectedMovies, resultMoviesMapped);
    }

    @Test
    @Order(2)
    @DisplayName("getMoviesByParams: пустой результат при корректных параметрах запроса")
    public void getMoviesByParams_ValidArgument_EmptyList() throws OperationException {
        Movie movieParams = new Movie();
        movieParams.setCountry("Canada");

        List<Movie> resultMovies = movieService.getMoviesByParams(movieParams);
        List<MovieMapped> resultMoviesMapped = resultMovies.stream().map(MovieMapped::ofMovie).toList();

        List<MovieMapped> expectedMovies = new LinkedList<>();

        Assertions.assertEquals(expectedMovies, resultMoviesMapped);
    }

    @Test
    @Order(3)
    @DisplayName("getMoviesByParams: исключение при пустых параметрах запроса")
    public void getMovieByParams_EmptyArgument_ExceptionThrown() {
        Movie movieParams = new Movie();

        OperationException operationException = Assertions.assertThrows(OperationException.class, () ->
                movieService.getMoviesByParams(movieParams)
        );
        Assertions.assertEquals("The argument is empty", operationException.getMessage());

        System.out.println(operationException.getMessage() + "\n" + operationException.getFaultInfo().getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("getMoviesByParams: исключение при некорректных параметрах запроса")
    public void getMoviesByParams_InvalidArgument_ThrownException() {
        Movie movieParams = new Movie();
        movieParams.setCountry("");
        movieParams.setDuration(99999);

        OperationException operationException = Assertions.assertThrows(OperationException.class, () ->
            movieService.getMoviesByParams(movieParams)
        );
        Assertions.assertEquals("The argument has incorrect fields", operationException.getMessage());

        System.out.println(operationException.getMessage() + "\n" + operationException.getFaultInfo().getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("createMovie: успешное создание фильма при корректном аргументе movie")
    public void createMovie_ValidArgument_MovieCreated() throws OperationException {
        Movie movie = new Movie();
        movie.setName("Back to the Future");
        movie.setReleaseDate(1985);
        movie.setCountry("USA");
        movie.setDuration(116);

        Long createdMovieId = movieService.createMovie(movie);

        Assertions.assertTrue(createdMovieId > 0);
    }

    @Test
    @Order(6)
    @DisplayName("createMovie: исключение при отсутствии обязательных полей в аргументе movie")
    public void createMovie_NotAllRequiredFields_MovieCreated() {
        Movie movie = new Movie();
        movie.setReleaseDate(1985);
        movie.setDuration(116);

        OperationException operationException = Assertions.assertThrows(OperationException.class, () ->
                movieService.createMovie(movie)
        );
        Assertions.assertEquals("The argument does not contain all required fields", operationException.getMessage());

        System.out.println(operationException.getMessage() + "\n" + operationException.getFaultInfo().getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("createMovie: исключение при некорректных полях movie")
    public void createMovie_InvalidFields_ThrownException() {
        Movie movie = new Movie();
        movie.setName("Back to the Future");
        movie.setReleaseDate(-2);
        movie.setCountry(" ");
        movie.setDuration(0);

        OperationException operationException = Assertions.assertThrows(OperationException.class, () ->
                movieService.createMovie(movie)
        );
        Assertions.assertEquals("The argument has incorrect fields", operationException.getMessage());

        System.out.println(operationException.getMessage() + "\n" + operationException.getFaultInfo().getMessage());
    }

    @Test
    @Order(8)
    @DisplayName("updateMovie: успешное обновление фильма при корректных аргументах movieId, movie")
    public void updateMovie_ValidArgument_MovieUpdated() throws OperationException {
        Long movieId = 1L;

        Movie movie = new Movie();
        movie.setCountry("Italy");
        movie.setDuration(90);

        String resultResponse = movieService.updateMovie(movieId, movie);

        Assertions.assertEquals("OK", resultResponse);
    }

    @Test
    @Order(9)
    @DisplayName("updateMovie: исключение при обновлении фильма с несуществующим movieId")
    public void updateMovie_NotExistingId_ThrownException() {
        Long movieId = 999L;

        Movie movie = new Movie();
        movie.setCountry("Italy");
        movie.setDuration(90);

        OperationException operationException = Assertions.assertThrows(OperationException.class, () ->
                movieService.updateMovie(movieId, movie)
        );
        Assertions.assertEquals("No entry with the specified ID", operationException.getMessage());

        System.out.println(operationException.getMessage() + "\n" + operationException.getFaultInfo().getMessage());
    }

    @Test
    @Order(10)
    @DisplayName("deleteMovie: исключение при удалении фильма с несуществующим movieId")
    public void deleteMovie_NotExistingId_ThrownException() {
        Long movieId = 999L;

        OperationException operationException = Assertions.assertThrows(OperationException.class, () ->
                movieService.deleteMovie(movieId)
        );
        Assertions.assertEquals("No entry with the specified ID", operationException.getMessage());

        System.out.println(operationException.getMessage() + "\n" + operationException.getFaultInfo().getMessage());
    }
}
