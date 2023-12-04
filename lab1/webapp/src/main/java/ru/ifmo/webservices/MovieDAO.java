package ru.ifmo.webservices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieDAO {
    private static Connection connection;
    private final String TABLE_NAME = "Movies";
    public MovieDAO() {
        try {
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/webserviceDB");
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, "Failed accessing the database", e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> getAllMovies() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + this.TABLE_NAME);
            return resultSetToMovies(rs);
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, "Failed accessing the database", e);
        }
        return new ArrayList<>();
    }

    public List<Movie> getMoviesByParams(Movie movieRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> paramsMap = objectMapper.convertValue(movieRequest, new TypeReference<>() {});

        String preparedQuery = createPreparedQuery(this.TABLE_NAME, paramsMap);
        try {
            PreparedStatement stmt = connection.prepareStatement(preparedQuery);
            fillPreparedStatement(stmt, paramsMap);
            ResultSet rs = stmt.executeQuery();
            return resultSetToMovies(rs);
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, "Failed accessing the database", e);
        }
        return new ArrayList<>();
    }

    private String createPreparedQuery(String tableName, Map<String, Object> paramsMap) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ");
        queryBuilder.append(tableName);

        if (paramsMap == null || paramsMap.isEmpty()) {
            return queryBuilder.toString();
        }

        queryBuilder.append(" WHERE 1=1");
        for (String key : paramsMap.keySet()) {
            queryBuilder.append(" AND ").append(key).append("=?");
        }
        queryBuilder.append(";");

        return queryBuilder.toString();
    }

    private void fillPreparedStatement(PreparedStatement statement, Map<String, Object> paramsMap) {
        int i = 1;
        try {
            for (String key : paramsMap.keySet()) {
                statement.setObject(i++, paramsMap.get(key));
            }
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException(e);
        }
    }

    private List<Movie> resultSetToMovies(ResultSet rs) {
        List<Movie> movies = new ArrayList<>();
        try {
            while (rs != null && rs.next()) {
                String name = rs.getString("name");
                int releaseDate = rs.getInt("release_date");
                String country = rs.getString("country");
                int duration = rs.getInt("duration");
                Movie movie = new Movie(name, releaseDate, country, duration);
                movies.add(movie);
            }
        } catch (SQLException e) {
            Logger.getLogger(MovieDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException(e);
        }
        return movies;
    }
}
