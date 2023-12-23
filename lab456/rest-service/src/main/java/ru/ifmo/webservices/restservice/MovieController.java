package ru.ifmo.webservices.restservice;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public List<MovieDTO> getMovies(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) Integer releaseDate,
                                    @RequestParam(required = false) String country,
                                    @RequestParam(required = false) Integer duration) {
        List<Movie> movies = movieRepository.findMoviesByParams(name, releaseDate, country, duration);
        return movies.stream().map(MovieDTO::of).toList();
    }

    @GetMapping(path = "/{movieId}")
    public MovieDTO getMovie(@PathVariable Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IdNotFoundException("Could not find the movie with id=" + movieId));

        return MovieDTO.of(movie);
    }

    @PostMapping
    public Long createMovie(@RequestBody @Valid MovieDTO movieDTO) {
        Movie movie = movieDTO.toMovie();
        Movie createdMovie = movieRepository.save(movie);
        return createdMovie.getId();
    }

    @PutMapping(path = "/{movieId}")
    public String updateMovie(@PathVariable Long movieId, @RequestBody @Valid MovieDTO movieDTO) {
         if (!movieRepository.existsById(movieId)) {
             throw new IdNotFoundException("Could not find the movie with id=" + movieId);
         }

        Movie movie = movieDTO.toMovie();
        movie.setId(movieId);
        movieRepository.save(movie);
        return "OK";
    }

    @DeleteMapping(path = "/{movieId}")
    public String deleteMovie(@PathVariable Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new IdNotFoundException("Could not find the movie with id=" + movieId);
        }

        movieRepository.deleteById(movieId);
        return "OK";
    }
}
