package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.StorageTest;

import java.time.LocalDate;

class InMemoryFilmStorageTest extends StorageTest<Film> {

    @BeforeEach
    void create() {
        storage = new InMemoryFilmStorage();
        t1 = Film.builder()
                .name("Green Mile")
                .description("The cinema about big black guy have got a superforce")
                .releaseDate(LocalDate.ofEpochDay(1975-5-29))
                .duration(100)
                .build();
        t2 = Film.builder()
                .name("Snatch")
                .description("The cinema about the diamond")
                .releaseDate(LocalDate.ofEpochDay(1998-5-29))
                .duration(102)
                .build();
        t3 = Film.builder()
                .name("RockNRolla")
                .description("The cinema about the Rock star Johnny Quid")
                .releaseDate(LocalDate.ofEpochDay(2008-5-29))
                .duration(102)
                .build();
    }

    @Test
    public void shouldThrowExceptionWhenDeleteFilmWithNotExistingId() {
        super.shouldThrowExceptionWhenDeleteNotExistingIdOne("Фильма c id = 1 не существует");
    }

    @Test
    public void shouldThrowExceptionWhenGetFilmByNotExistingIdOne() {
        super.shouldThrowExceptionWhenGetByNotExistingIdOne("Фильма c id = 1 не существует");
    }
    //    UserStorage userStorage;
//    User user1;
//    User user2;
//    User user3;
//
//    @BeforeEach
//    void createStorage() {
//        userStorage = new InMemoryUserStorage();
//        user1 = User.builder()
//                .name("Петя")
//                .login("SexInstructor")
//                .birthday(LocalDate.of(1990, 8, 13))
//                .email("petyasexinstructor@yandex.ru")
//                .build();
//        user2 = User.builder()
//                .name("Вася")
//                .login("Danger")
//                .birthday(LocalDate.of(1991, 8, 13))
//                .email("danger@yandex.ru")
//                .build();
//        user3 = User.builder()
//                .name("Коля")
//                .login("Fear")
//                .birthday(LocalDate.of(1989, 8, 13))
//                .email("fear@yandex.ru")
//                .build();
//    }
//
//    @Test
//    void shouldAddOneUser() {
//        userStorage.add(user1);
//        System.out.println(user1);
//        assertAll(
//                () -> assertEquals(1, userStorage.getAll().size()),
//                () -> assertEquals(tesUser, userStorage.getById(1))
//        );
//    }
//
//    @Test
//    void shouldIgnoreAddingUserWithEqualEmail() {
//        userStorage.add(user1);
//        user2 = user2.toBuilder()
//                .email(user1.getEmail())
//                .build();
//        userStorage.add(user2);
//        assertEquals(1, userStorage.getAll().size());
//    }


}