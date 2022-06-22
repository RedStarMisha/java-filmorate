package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.StorageTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryUserStorageTest extends StorageTest<User> {

    @BeforeEach
    void create() {
        storage = new InMemoryUserStorage();
        t1 = User.builder()
                .name("Петя")
                .login("SexInstructor")
                .birthday(LocalDate.of(1990, 8, 13))
                .email("petyasexinstructor@yandex.ru")
                .build();
        t2 = User.builder()
                .name("Вася")
                .login("Danger")
                .birthday(LocalDate.of(1991, 8, 13))
                .email("danger@yandex.ru")
                .build();
        t3 = User.builder()
                .name("Коля")
                .login("Fear")
                .birthday(LocalDate.of(1989, 8, 13))
                .email("fear@yandex.ru")
                .build();
    }

    @Test
    void shouldThrowExceptionWhenDeleteUserWithNotExistingId() {
        super.shouldThrowExceptionWhenDeleteNotExistingIdOne("Пользователя c id = 1 не существует");
    }

    @Test
    void shouldThrowExceptionWhenGetUserByNotExistingIdOne() {
        super.shouldThrowExceptionWhenGetByNotExistingIdOne("Пользователя c id = 1 не существует");
    }

    @Test
    void shouldSetLoginInsteadEmptyNameAfterAdd() {
        t1 = t1.toBuilder()
                .name("")
                .build();
        storage.add(t1);
        assertEquals(t1.getLogin(), storage.getById(1).getName());
    }

    @Test
    void shouldSetLoginInsteadNullNameAfterAdd() {
        t1 = t1.toBuilder()
                .name(null)
                .build();
        storage.add(t1);
        assertEquals(t1.getLogin(), storage.getById(1).getName());
    }

    @Test
    void shouldSetLoginInsteadEmptyNameAfterUpdate() {
        storage.add(t1);
        t1 = t1.toBuilder()
                .name("")
                .login("Flash")
                .build();
        storage.update(t1);
        assertEquals(t1.getLogin(), storage.getById(1).getName());
    }
}