package ru.ifmo.webservices;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "movie")
@XmlType(name = "movie", propOrder = {
        "name",
        "releaseDate",
        "country",
        "duration"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {
    private String name;
    @JsonProperty("release_date")
    private Integer releaseDate;
    private String country;
    private Integer duration;

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
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", country='" + country + '\'' +
                ", duration=" + duration +
                '}';
    }
}
