package ru.ifmo.webservices.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ifmo.webservices.exceptions.ExceptionMessage;
import ru.ifmo.webservices.exceptions.MovieServiceFault;
import ru.ifmo.webservices.exceptions.OperationException;
import ru.ifmo.webservices.utils.DatabaseConnection;
import ru.ifmo.webservices.model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieDAO {
    private static Connection connection;
    private static ObjectMapper objectMapper;

    private enum QueryTemplate {
        SELECT("SELECT * FROM movies"),
        INSERT("INSERT INTO movies(name, release_date, country, duration) VALUES (?, ?, ?, ?)"),
        UPDATE("UPDATE movies"),
        DELETE("DELETE FROM movies WHERE id=?");

        private final String query;

        QueryTemplate(String query) {
            this.query = query;
        }

        public String query() {
            return query;
        }
    }

    public MovieDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            objectMapper = new ObjectMapper();
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, "Failed accessing the database", e);
            throw new RuntimeException(e);
        }
    }

    public List<Movie> getAllMovies() throws OperationException {
        ResultSet rs = null;
        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(QueryTemplate.SELECT.query());
        } catch (SQLException e) {
            logAndThrow(e, ExceptionMessage.INTERNAL_ERROR, "Error happened while executing SQL query");
        }
        return resultSetToMovies(rs);
    }

    public List<Movie> getMoviesByParams(Movie movieRequest) throws OperationException {
        Map<String, Object> paramsMap = objectMapper.convertValue(movieRequest, new TypeReference<>() {});

        ResultSet rs = null;
        String preparedQuery = createPreparedQuerySelect(paramsMap);
        try {
            PreparedStatement stmt = connection.prepareStatement(preparedQuery);
            fillPreparedStatement(stmt, paramsMap);
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            logAndThrow(e, ExceptionMessage.INTERNAL_ERROR, "Error happened while executing SQL query");
        }
        return resultSetToMovies(rs);
    }

    public Long createMovie(Movie movie) throws OperationException {
        Map<String, Object> movieMap = objectMapper.convertValue(movie, new TypeReference<>() {});

        Long resultMovieId = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(QueryTemplate.INSERT.query(), Statement.RETURN_GENERATED_KEYS);
            fillPreparedStatement(stmt, movieMap);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new OperationException(ExceptionMessage.INTERNAL_ERROR.message(), new MovieServiceFault("Creating movie failed"));
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                resultMovieId = generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            logAndThrow(e, ExceptionMessage.INTERNAL_ERROR, "Error happened while executing SQL query");
        }
        return resultMovieId;
    }

    public Long updateMovie(Long movieId, Movie movie) throws OperationException {
        Map<String, Object> movieMap = objectMapper.convertValue(movie, new TypeReference<>() {});

        String preparedQuery = createPreparedQueryUpdate(movieId, movieMap);
        Long resultMovieId = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(preparedQuery);
            fillPreparedStatement(stmt, movieMap);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new OperationException(ExceptionMessage.NOT_EXISTING_ID.message(), new MovieServiceFault("Cannot update movie with ID = " + movieId));
            }
            resultMovieId = movieId;
        } catch (SQLException e) {
            logAndThrow(e, ExceptionMessage.INTERNAL_ERROR, "Error happened while executing SQL query");
        }
        return resultMovieId;
    }

    public Long deleteMovie(Long movieId) throws OperationException {
        Long resultMovieId = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(QueryTemplate.DELETE.query());
            stmt.setObject(1, movieId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new OperationException(ExceptionMessage.NOT_EXISTING_ID.message(), new MovieServiceFault("Cannot delete movie with ID = " + movieId));
            }
            resultMovieId = movieId;
        } catch (SQLException e) {
            logAndThrow(e, ExceptionMessage.INTERNAL_ERROR, "Error happened while executing SQL query");
        }
        return resultMovieId;
    }

    private String createPreparedQuerySelect(Map<String, Object> entityMap) {
        StringBuilder queryBuilder = new StringBuilder(QueryTemplate.SELECT.query());

        if (entityMap == null || entityMap.isEmpty()) {
            return queryBuilder.toString();
        }

        queryBuilder.append(" WHERE 1=1");
        for (String key : entityMap.keySet()) {
            queryBuilder.append(" AND ").append(key).append("=?");
        }
        queryBuilder.append(";");

        return queryBuilder.toString();
    }

    private String createPreparedQueryUpdate(Long entityId, Map<String, Object> entityMap) {
        StringBuilder queryBuilder = new StringBuilder(QueryTemplate.UPDATE.query());

        if (entityMap == null || entityMap.isEmpty()) {
            throw new RuntimeException("No parameters for update query");
        }

        queryBuilder.append(" SET");
        int i = 1;
        for (String key : entityMap.keySet()) {
            queryBuilder.append(" ").append(key).append("=?");
            if (i++ != entityMap.keySet().size()) {
                queryBuilder.append(",");
            }
        }

        queryBuilder.append(" WHERE id=").append(entityId).append(";");

        return queryBuilder.toString();
    }

    private void fillPreparedStatement(PreparedStatement statement, Map<String, Object> paramsMap) throws SQLException {
        int i = 1;
        for (String key : paramsMap.keySet()) {
            statement.setObject(i++, paramsMap.get(key));
        }
    }

    private List<Movie> resultSetToMovies(ResultSet rs) throws OperationException {
        List<Movie> movies = new ArrayList<>();
        try {
            while (rs != null && rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getLong("id"));
                movie.setName(rs.getString("name"));
                movie.setReleaseDate(rs.getInt("release_date"));
                movie.setCountry(rs.getString("country"));
                movie.setDuration(rs.getInt("duration"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            logAndThrow(e, ExceptionMessage.INTERNAL_ERROR, "");
        }        return movies;
    }

    private void logAndThrow(Exception e, ExceptionMessage exceptionMessage, String exceptionDetails) throws OperationException {
        Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, exceptionMessage.message(), e);
        throw new OperationException(
                exceptionMessage.message(),
                new MovieServiceFault(exceptionDetails));
    }
}
