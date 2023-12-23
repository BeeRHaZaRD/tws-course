package ru.ifmo.webservices;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Movie(Long id, String name, Integer releaseDate, String country, Integer duration) {
    public Movie(String name, Integer releaseDate, String country, Integer duration) {
        this(null, name, releaseDate, country, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(name, movie.name) && Objects.equals(releaseDate, movie.releaseDate) && Objects.equals(country, movie.country) && Objects.equals(duration, movie.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, releaseDate, country, duration);
    }
}
