package ru.ifmo.webservices.restservice;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer releaseDate;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private Integer duration;

    public Movie() {}

    public Movie(Long id, String name, Integer releaseDate, String country, Integer duration) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.country = country;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Integer releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return name.equals(movie.name) && Objects.equals(releaseDate, movie.releaseDate) && Objects.equals(country, movie.country) && Objects.equals(duration, movie.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, releaseDate, country, duration);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", country='" + country + '\'' +
                ", duration=" + duration +
                '}';
    }
}
