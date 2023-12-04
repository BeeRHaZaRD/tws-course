package ru.ifmo.webservices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieDAO {
    private static Connection connection;
    private static ObjectMapper objectMapper;

    private final String SELECT_QUERY = "SELECT * FROM movies";
    private final String INSERT_QUERY = "INSERT INTO movies(name, release_date, country, duration) VALUES (?, ?, ?, ?)";
    private final String UPDATE_QUERY = "UPDATE movies";
    private final String DELETE_QUERY = "DELETE FROM movies WHERE id=?";
    public MovieDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            objectMapper = new ObjectMapper();
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, "Failed accessing the database", e);
            throw new RuntimeException(e);
        }
    }

    public List<Movie> getAllMovies() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_QUERY);
            return resultSetToMovies(rs);
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ArrayList<>();
    }

    public List<Movie> getMoviesByParams(Movie movieRequest) {
        Map<String, Object> paramsMap = objectMapper.convertValue(movieRequest, new TypeReference<>() {});
        if (paramsMap.isEmpty()) {
            return new ArrayList<>();
        }

        String preparedQuery = createPreparedQuerySelect(paramsMap);
        ResultSet rs = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(preparedQuery);
            fillPreparedStatement(stmt, paramsMap);
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return resultSetToMovies(rs);
    }

    public Long createMovie(Movie movie) {
        Map<String, Object> movieMap = objectMapper.convertValue(movie, new TypeReference<>() {});

        Long resultMovieId = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            fillPreparedStatement(stmt, movieMap);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating movie failed, no rows affected");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    resultMovieId = generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return resultMovieId;
    }

    public Long updateMovie(Long movieId, Movie movie) {
        Map<String, Object> movieMap = objectMapper.convertValue(movie, new TypeReference<>() {});

        String preparedQuery = createPreparedQueryUpdate(movieId, movieMap);
        Long resultMovieId = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(preparedQuery);
            fillPreparedStatement(stmt, movieMap);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating movie failed, no rows affected");
            }
            resultMovieId = movieId;
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return resultMovieId;
    }

    public Long deleteMovie(Long movieId) {
        Long resultMovieId = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(DELETE_QUERY);
            stmt.setObject(1, movieId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating movie failed, no rows affected");
            }
            resultMovieId = movieId;
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return resultMovieId;
    }

    private String createPreparedQuerySelect(Map<String, Object> entityMap) {
        StringBuilder queryBuilder = new StringBuilder(SELECT_QUERY);

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
        StringBuilder queryBuilder = new StringBuilder(UPDATE_QUERY);

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

    private List<Movie> resultSetToMovies(ResultSet rs) {
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
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return movies;
    }
}
