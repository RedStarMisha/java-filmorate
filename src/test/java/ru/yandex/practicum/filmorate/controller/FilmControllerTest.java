package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController controller;
    FilmStorage filmStorage;
    UserStorage userStorage;
    Film f1;
    Film f2;
    Film f3;
    User u1;
    User u2;
    User u3;

    @BeforeEach
    void createNewController() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        controller = new FilmController(filmStorage,
                new FilmService(filmStorage, userStorage));
        f1 = Film.builder()
                .name("Green Mile")
                .description("The cinema about big black guy have got a superforce")
                .releaseDate(LocalDate.ofEpochDay(1975-5-29))
                .duration(100)
                .build();
        f2 = Film.builder()
                .name("Snatch")
                .description("The cinema about the diamond")
                .releaseDate(LocalDate.ofEpochDay(1998-5-29))
                .duration(102)
                .build();
        f3 = Film.builder()
                .name("RockNRolla")
                .description("The cinema about the Rock star Johnny Quid")
                .releaseDate(LocalDate.ofEpochDay(2008-5-29))
                .duration(102)
                .build();
        u1 = User.builder()
                .name("Петя")
                .login("SexInstructor")
                .birthday(LocalDate.of(1990, 8, 13))
                .email("petyasexinstructor@yandex.ru")
                .build();
        u2 = User.builder()
                .name("Вася")
                .login("Danger")
                .birthday(LocalDate.of(1991, 8, 13))
                .email("danger@yandex.ru")
                .build();
        u3 = User.builder()
                .name("Коля")
                .login("Fear")
                .birthday(LocalDate.of(1989, 8, 13))
                .email("fear@yandex.ru")
                .build();
    }

    @Test
    public void shouldCheckMyDateVilidatorWhenDateBeforeCinemaBirthday() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = factory.getValidator();
        Film newFilm = f1.toBuilder()
                .releaseDate(LocalDate.of(1894,12,28))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldCheckMyDateVilidatorWhenDateAfterActualDate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = factory.getValidator();
        Film newFilm = f1.toBuilder()
                .releaseDate(LocalDate.now().plusDays(1))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAddNewFilm() {
        controller.addNewFilm(f1);
        assertAll(
                () -> assertEquals(1, controller.getAllFilms().size()),
                () -> assertEquals(f1, filmStorage.getById(1))
        );
        }

    @Test
    void shouldUpdateFilm() {
        controller.addNewFilm(f1);
        f2.setId(1);
        controller.updateFilm(f2);
        assertEquals(1, filmStorage.getAll().size());
        assertEquals(f2, filmStorage.getById(1));
    }

    @Test
    void shouldAddedLikeToFilm() {
        filmStorage.add(f1);
        userStorage.add(u1);
        controller.addLikeToFilm(1L, 1L);
        assertEquals(f1.getIdUserWhoLikedSet().size(), 1);
        assertTrue(f1.getIdUserWhoLikedSet().contains(1L));
    }



    @Test
    void shouldDeleteFilmWithIdOne() {
        filmStorage.add(f1);
        controller.deleteFilmData(1L, null);
        assertTrue(controller.getAllFilms().isEmpty());
    }

    @Test
    void shouldDeleteLikeFromFilm() {
        filmStorage.add(f1);
        userStorage.add(u1);
        userStorage.add(u2);
        controller.addLikeToFilm(1L, 1L);
        controller.addLikeToFilm(1L, 2L);
        controller.deleteFilmData(1L, 1L);
        assertEquals(filmStorage.getById(1).getIdUserWhoLikedSet().size(), 1);
        assertTrue(filmStorage.getById(1).getIdUserWhoLikedSet().contains(2L));
    }

    @Test
    void shouldGetAllFilms() {
        controller.addNewFilm(f1);
        controller.addNewFilm(f2);
        controller.addNewFilm(f3);
        Set<Film> testSet = Set.of(f1, f2, f3);
        assertEquals(testSet, controller.getAllFilms());
    }

    @Test
    void shouldGetFilmById() {
        controller.addNewFilm(f1);
        Film testFilm = controller.getFilmById(1L);
        assertEquals(testFilm, f1);
    }

    @Test
    void shouldGetMostPopularFilmsWithoutCountValue() {
        filmStorage.add(f1);
        filmStorage.add(f2);
        filmStorage.add(f3);
        userStorage.add(u1);
        userStorage.add(u2);
        userStorage.add(u3);
        controller.addLikeToFilm( 1L, 1L);
        controller.addLikeToFilm( 2L, 1L);
        controller.addLikeToFilm( 2L, 2L);
        controller.addLikeToFilm( 3L, 1L);
        controller.addLikeToFilm( 3L, 2L);
        controller.addLikeToFilm( 3L, 3L);
        List<Film> testPopularFilms = List.of(f3, f2);
        List<Film> popularFilms = controller.getMostPopularFilms(2L);
        assertEquals(testPopularFilms, popularFilms);
    }
}