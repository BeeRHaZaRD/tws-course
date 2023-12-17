package ru.ifmo.webservices.validators;

import ru.ifmo.webservices.model.Movie;
import ru.ifmo.webservices.utils.Constraint;
import ru.ifmo.webservices.utils.AbstractValidator;

public class MovieValidator extends AbstractValidator {
    public MovieValidator(Movie movie, boolean notEmptyRequired, RequestType requestType) {
        super(notEmptyRequired);

        Constraint<String> nameConstraint = new Constraint<>("name", movie.getName(), (name) -> !name.isBlank(), "not blank");
        Constraint<Integer> releaseDateConstraint = new Constraint<>("releaseDate", movie.getReleaseDate(), (releaseDate) -> releaseDate > 1800, "more than 1800");
        Constraint<String> countryConstraint = new Constraint<>("country", movie.getCountry(), (country) -> !country.isBlank(), "not blank");
        Constraint<Integer> durationConstraint = new Constraint<>("duration", movie.getDuration(), (duration) -> duration > 0 && duration < 720, "more than 0 and less than 720 minutes");

        switch (requestType) {
            case CREATE -> {
                addConstraint(nameConstraint, true);
                addConstraint(releaseDateConstraint, true);
                addConstraint(countryConstraint, true);
                addConstraint(durationConstraint, true);
            }
            case UPDATE, PARAMS -> {
                addConstraint(nameConstraint, false);
                addConstraint(releaseDateConstraint, false);
                addConstraint(countryConstraint, false);
                addConstraint(durationConstraint, false);
            }
        }
    }
}
