package ru.ifmo.webservices.restservice;

import jakarta.validation.constraints.*;

public record MovieDTO(
        Long id,

        @NotBlank(message = "Invalid name: must be not empty")
        String name,

        @NotNull(message = "No required param: releaseDate")
        @Min(value = 1700, message = "Invalid releaseDate: must be more than {value}")
        Integer releaseDate,

        @NotBlank(message = "Invalid country: must be not empty")
        String country,

        @NotNull(message = "No required param: duration")
        @Min(value = 1, message = "Invalid duration: must be a positive number")
        @Max(value = 720, message = "Invalid duration: must be less than {value}")
        Integer duration) {

    public Movie toMovie() {
        return new Movie(id, name, releaseDate, country, duration);
    }

    public static MovieDTO of(Movie movie) {
        return movie != null
                ? new MovieDTO(movie.getId(), movie.getName(), movie.getReleaseDate(), movie.getCountry(), movie.getDuration())
                : null;
    }
}
