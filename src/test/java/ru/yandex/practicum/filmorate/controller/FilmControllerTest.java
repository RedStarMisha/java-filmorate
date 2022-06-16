package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest extends UtilControllerTest<Film> {

    @BeforeEach
    void createNewController() {
        controller = new FilmController();
         element = Film.builder()
                .id(0)
                .name("Green Mile")
                .description("The cinema about big black guy have got a superforce")
                .releaseDate(LocalDate.ofEpochDay(1975-5-29))
                .duration(100)
                .build();
    }

    @Test
    void shouldAddNewFilm() throws ValidationException {
        Film addedFilm =  controller.addNewElement(element);
        assertAll(
                () -> assertEquals(1, controller.getAllElements().size()),
                () -> assertEquals(addedFilm.getId(), 1),
                () -> assertEquals(addedFilm.getName(), element.getName()),
                () -> assertEquals(addedFilm.getDescription(), element.getDescription()),
                () -> assertEquals(addedFilm.getDuration(), element.getDuration()),
                () -> assertEquals(addedFilm.getReleaseDate(), element.getReleaseDate())
        );
        }

    @Test
    public void shouldCheckMyDateVilidatorWhenDateBeforeCinemaBirthday() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = factory.getValidator();
        Film newFilm = element.toBuilder()
                        .releaseDate(LocalDate.of(1894,12,28))
                        .build();
        System.out.println(newFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldCheckMyDateVilidatorWhenDateAfterActualDate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = factory.getValidator();
        Film newFilm = element.toBuilder()
                .releaseDate(LocalDate.now().plusDays(1))
                .build();
        System.out.println(newFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        assertFalse(violations.isEmpty());
    }
}