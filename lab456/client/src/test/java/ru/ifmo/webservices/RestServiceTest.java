package ru.ifmo.webservices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestServiceTest {

    private final String baseUrl = "http://localhost:8080";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Order(1)
    @DisplayName("getMovies: все фильмы при пустых параметрах")
    public void getMovies_ValidNoParams_AllMovies() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Movie> actualMovies = objectMapper.readValue(response.body(), new TypeReference<>() {});

        Assertions.assertEquals(12, actualMovies.size());
    }

    @Test
    @Order(2)
    @DisplayName("getMovies: корректный результат при корректных параметрах запроса")
    public void getMovies_ValidParams_ValidMovies() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies?country=France&duration=110"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Movie> actualMovies = objectMapper.readValue(response.body(), new TypeReference<>() {});

        List<Movie> expectedMovies = new ArrayList<>();
        expectedMovies.add(new Movie("Leon", 1994, "France", 110));
        expectedMovies.add(new Movie("Billy Elliot", 2000, "France", 110));

        Assertions.assertEquals(expectedMovies, actualMovies);
    }

    @Test
    @Order(3)
    @DisplayName("getMovie: корректный результат при корректном ID")
    public void getMovie_ValidParam_ValidMovie() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies/1"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Movie actualMovie = objectMapper.readValue(response.body(), Movie.class);

        Movie expectedMovie = new Movie("Leon", 1994, "France", 110);

        Assertions.assertEquals(expectedMovie, actualMovie);
    }

    @Test
    @Order(4)
    @DisplayName("getMovie: ошибка при некорректном ID")
    public void getMovie_NotExistingId_ErrorNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies/404"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode responseNode = objectMapper.readTree(response.body());

        Assertions.assertEquals(404, responseNode.get("status").asInt());
        Assertions.assertEquals("Could not find the movie with id=404", responseNode.get("error").asText());
    }

    @Test
    @Order(5)
    @DisplayName("createMovie: успешное создание фильма при корректном аргументе movie")
    public void createMovie_ValidParam_MovieCreated() throws IOException, InterruptedException {
        Movie movie = new Movie("Back to the Future", 1985, "USA", 116);

        String requestBody = objectMapper.writeValueAsString(movie);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int actualId = Integer.parseInt(response.body());

        Assertions.assertTrue(actualId > 0);
    }

    @Test
    @Order(6)
    @DisplayName("createMovie: ошибка при отсутствии обязательных полей в параметре movie")
    public void createMovie_NoRequiredFields_ErrorBadRequest() throws IOException, InterruptedException {
        Movie movie = new Movie(null, 1985, null, 116);

        String requestBody = objectMapper.writeValueAsString(movie);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode responseNode = objectMapper.readTree(response.body());

        Assertions.assertEquals(400, responseNode.get("status").asInt());
        Assertions.assertEquals("Bad Request", responseNode.get("error").asText());

        for (JsonNode errorNode : responseNode.get("errorDetails")) {
            System.out.println(errorNode.asText());
        }
    }

    @Test
    @Order(7)
    @DisplayName("createMovie: ошибка при некорректных полях movie")
    public void createMovie_InvalidFields_ErrorBadRequest() throws IOException, InterruptedException {
        Movie movie = new Movie("  ", 200, "", 0);

        String requestBody = objectMapper.writeValueAsString(movie);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode responseNode = objectMapper.readTree(response.body());

        Assertions.assertEquals(400, responseNode.get("status").asInt());
        Assertions.assertEquals("Bad Request", responseNode.get("error").asText());

        for (JsonNode errorNode : responseNode.get("errorDetails")) {
            System.out.println(errorNode.asText());
        }
    }

    @Test
    @Order(8)
    @DisplayName("updateMovie: успешное обновление фильма при корректном аргументе movie")
    public void updateMovie_ValidParam_MovieUpdated() throws IOException, InterruptedException {
        int movieId = 10;
        Movie movie = new Movie("New name", 2023, "Russia", 100);

        String requestBody = objectMapper.writeValueAsString(movie);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies/" + movieId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals("OK", response.body());

        request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies/" + movieId))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Movie actualMovie = objectMapper.readValue(response.body(), Movie.class);

        Assertions.assertEquals(movie, actualMovie);
    }

    @Test
    @Order(9)
    @DisplayName("updateMovie: ошибка при отсутствии обязательных полей в параметре movie")
    public void updateMovie_NoRequiredFields_ErrorBadRequest() throws IOException, InterruptedException {
        int movieId = 10;
        Movie movie = new Movie(null, 2023, null, 100);

        String requestBody = objectMapper.writeValueAsString(movie);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies/" + movieId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode responseNode = objectMapper.readTree(response.body());

        Assertions.assertEquals(400, responseNode.get("status").asInt());
        Assertions.assertEquals("Bad Request", responseNode.get("error").asText());

        for (JsonNode errorNode : responseNode.get("errorDetails")) {
            System.out.println(errorNode.asText());
        }
    }

    @Test
    @Order(10)
    @DisplayName("updateMovie: ошибка при некорректных полях movie")
    public void updateMovie_InvalidFields_ErrorBadRequest() throws IOException, InterruptedException {
        int movieId = 10;
        Movie movie = new Movie("  ", 200, "", 0);

        String requestBody = objectMapper.writeValueAsString(movie);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies/" + movieId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode responseNode = objectMapper.readTree(response.body());

        Assertions.assertEquals(400, responseNode.get("status").asInt());
        Assertions.assertEquals("Bad Request", responseNode.get("error").asText());

        for (JsonNode errorNode : responseNode.get("errorDetails")) {
            System.out.println(errorNode.asText());
        }
    }

    @Test
    @Order(11)
    @DisplayName("updateMovie: ошибка при некорректном ID")
    public void updateMovie_NotExistingId_ErrorNotFound() throws IOException, InterruptedException {
        int movieId = 404;
        Movie movie = new Movie("New name", 2023, "Russia", 100);

        String requestBody = objectMapper.writeValueAsString(movie);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies/" + movieId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode responseNode = objectMapper.readTree(response.body());

        Assertions.assertEquals(404, responseNode.get("status").asInt());
        Assertions.assertEquals("Could not find the movie with id=404", responseNode.get("error").asText());
    }

    @Test
    @Order(12)
    @DisplayName("deleteMovie: успешное удаление фильма")
    public void deleteMovie_ValidId_ErrorBadRequest() throws IOException, InterruptedException {
        int movieId = 10;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies/" + movieId))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals("OK", response.body());
    }

    @Test
    @Order(13)
    @DisplayName("deleteMovie: ошибка при некорректном ID")
    public void deleteMovie_NotExistingId_ErrorNotFound() throws IOException, InterruptedException {
        int movieId = 404;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/movies/" + movieId))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode responseNode = objectMapper.readTree(response.body());

        Assertions.assertEquals(404, responseNode.get("status").asInt());
        Assertions.assertEquals("Could not find the movie with id=404", responseNode.get("error").asText());
    }
}
