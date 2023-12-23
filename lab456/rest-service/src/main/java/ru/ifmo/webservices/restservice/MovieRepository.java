package ru.ifmo.webservices.restservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("select m from Movie m where " +
            "(:name is null or m.name = :name) and " +
            "(:releaseDate is null or m.releaseDate = :releaseDate) and " +
            "(:country is null or m.country = :country) and " +
            "(:duration is null or m.duration = :duration)")
    List<Movie> findMoviesByParams(
            @Param("name") String name,
            @Param("releaseDate") Integer releaseDate,
            @Param("country") String country,
            @Param("duration") Integer duration);
}