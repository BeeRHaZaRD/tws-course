package ru.ifmo.webservices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "movie")
@XmlType(name = "movie", propOrder = {"id", "name", "releaseDate", "country", "duration"})
@JsonPropertyOrder({"id", "name", "releaseDate", "country", "duration"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {
    @JsonIgnore
    private Long id;
    private String name;
    @JsonProperty("release_date")
    private Integer releaseDate;
    private String country;
    private Integer duration;

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
    public String toString() {
        return "Movie{" +
            "name='" + name + '\'' +
            ", releaseDate=" + releaseDate +
            ", country='" + country + '\'' +
            ", duration=" + duration +
            '}';
    }
}
