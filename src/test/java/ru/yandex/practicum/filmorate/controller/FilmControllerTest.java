package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

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
    void shouldThrowExceptionWhenAddedFilmWithEmptyName() {
        element = element.toBuilder()
                .name("")
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.addNewElement(element));
        assertEquals(exception.getMessage(), "Название фильма не может быть пустым");
    }

    @Test
    void shouldThrowExceptionWhenAddedFilmWithNullName() {
        element = element.toBuilder()
                .name(null)
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.addNewElement(element));
        assertEquals(exception.getMessage(), "Название фильма не может быть пустым");
    }

    @Test
    void shouldThrowExceptionWhenFilmDescriptionMoreThanTwoHundredCharacters() {
        element = element.toBuilder()
                .description("We use a lot of the tools that come with the Spring framework and reap the benefits"
                        + "of having a lot of the out of the box solutions, and not having to worry about writing a ton"
                        + "of additional code—so that really saves us some time and energy.")
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.addNewElement(element));
        assertEquals(exception.getMessage(), "Не допустимое описание");
    }

    @Test
    void shouldThrowExceptionWhenReleaseDateIsNull() {
        element = element.toBuilder()
                .releaseDate(null)
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.addNewElement(element));
        assertEquals(exception.getMessage(), "Недопустимая дата релиза");
    }

    @Test
    void shouldThrowExceptionWhenReleaseDateBeforeCinemaBirthday() {
        element = element.toBuilder()
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.addNewElement(element));
        assertEquals(exception.getMessage(), "Недопустимая дата релиза");
    }

    @Test
    void shouldThrowExceptionWhenReleaseDateAfterCurrentDate() {
        element = element.toBuilder()
                .releaseDate(LocalDate.now().plusDays(1))
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.addNewElement(element));
        assertEquals(exception.getMessage(), "Недопустимая дата релиза");
    }

    @Test
    void shouldThrowExceptionThenFilmDurationIsNull() {
        element = element.toBuilder()
                .duration(0)
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.addNewElement(element));
        assertEquals(exception.getMessage(), "Недопустимая длительность");
    }

    @Test
    void shouldAddNewFilmUsingPostMethod() throws ValidationException {
        controller.addNewElement(element);
        element = element.toBuilder()
                .description("The cinema about Tom Hensk")
                .id(1)
                .build();
        Film addedFilm = controller.updateElement(element);
        assertEquals(1, controller.getAllElements().size());
        assertEquals("The cinema about Tom Hensk", addedFilm.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenUpdateFilmWithWrongId() throws ValidationException {
        controller.addNewElement(element);
        element = element.toBuilder()
                .description("The cinema about Tom Hensk")
                .id(-1)
                .build();
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.updateElement(element));
        assertEquals(exception.getMessage(), "id не может быть меньше 1");
    }
}