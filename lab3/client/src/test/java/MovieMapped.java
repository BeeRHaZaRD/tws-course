import ru.ifmo.webservices.generated.Movie;

import java.util.Objects;

public class MovieMapped {
    private final String name;
    private final Integer releaseDate;
    private final String country;
    private final Integer duration;

    public MovieMapped(String name, Integer releaseDate, String country, Integer duration) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.country = country;
        this.duration = duration;
    }

    public static MovieMapped ofMovie(Movie movie) {
        return new MovieMapped(
                movie.getName(),
                movie.getReleaseDate(),
                movie.getCountry(),
                movie.getDuration());
    }

    public String getName() {
        return name;
    }

    public Integer getReleaseDate() {
        return releaseDate;
    }

    public String getCountry() {
        return country;
    }

    public Integer getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieMapped that = (MovieMapped) o;
        return Objects.equals(name, that.name) && Objects.equals(releaseDate, that.releaseDate) && Objects.equals(country, that.country) && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, releaseDate, country, duration);
    }
}
