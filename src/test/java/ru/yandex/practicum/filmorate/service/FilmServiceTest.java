package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {

    private FilmService filmService;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    Film f1;
    Film f2;
    Film f3;
    User u1;
    User u2;
    User u3;

    @BeforeEach
    void create() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
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
        filmStorage.add(f1);
        filmStorage.add(f2);
        filmStorage.add(f3);
        userStorage.add(u1);
        userStorage.add(u2);
        userStorage.add(u3);
    }

    @Test
    void shouldAddLikeFromUserIdOneToFilmIdTwo() {
        filmService.addLikeToFilm(2, 1);
        assertEquals(1, filmStorage.getById(2).getIdUserWhoLikedSet().size());
        assertTrue(filmStorage.getById(2).getIdUserWhoLikedSet().contains(1L));
    }

    @Test
    void shouldDeleteOneLikeFromFilmWithIdTwo() {
        filmService.addLikeToFilm(2,2);
        filmService.addLikeToFilm(2, 1);
        filmService.deleteLikeFromFilm(2, 1);
        assertEquals(1, filmStorage.getById(2).getIdUserWhoLikedSet().size());
        assertTrue(filmStorage.getById(2).getIdUserWhoLikedSet().contains(2L));
    }

    @Test
    void shouldGetPopularFilmsWithCountEqualsTwo() {
        filmService.addLikeToFilm(1,1);
        filmService.addLikeToFilm(2,2);
        filmService.addLikeToFilm(2, 1);
        filmService.addLikeToFilm(3, 1);
        filmService.addLikeToFilm(3, 2);
        filmService.addLikeToFilm(3,3);
        List<Film> popularFilms = filmService.getPopularFilms(2);
        assertAll(
                () -> assertEquals(popularFilms.size(), 2),
                () -> assertEquals(popularFilms.get(0).getId(), 3),
                () -> assertEquals(popularFilms.get(1).getId(), 2)
        );
    }
}