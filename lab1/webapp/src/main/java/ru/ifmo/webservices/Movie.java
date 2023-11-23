package ru.ifmo.webservices;

public class Movie {
    private String name;
    private int releaseDate;
    private String country;
    private int duration;

    public Movie() {}
    public Movie(String name, int releaseDate, String country, int duration) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.country = country;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Movie{" +
            "name='" + name + '\'' +
            ", releaseDate=" + releaseDate +
            ", country='" + country + '\'' +
            ", duration=" + duration +
            '}';
    }
}
