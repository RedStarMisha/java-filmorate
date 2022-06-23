package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.EntityIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.UserIsNotExistingException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest<T> {
    public Storage<T> storage;
    public T t1;
    public T t2;
    public T t3;

    @Test
    void shouldAddThreeEntity() {
        storage.add(t1);
        storage.add(t2);
        storage.add(t3);
        Set<T> set = Set.of(t1, t2, t3);
        assertEquals(3, storage.getAll().size());
        assertEquals(set, storage.getAll());
    }


    @Test
    void shouldDeleteIdOne() throws FilmIsNotExistingException, UserIsNotExistingException {
        storage.add(t1);
        storage.add(t2);
        storage.delete(1);
        assertEquals(1, storage.getAll().size());
        assertEquals(t2, storage.getById(2));
    }

    @Test
    void shouldGetEmptySet() {
        assertEquals(new HashSet(), storage.getAll());
    }

    @Test
    void shouldGetThreeEntity() {
        storage.add(t1);
        storage.add(t2);
        storage.add(t3);
        Set<T> set = Set.of(t1, t2, t3);
        assertEquals(set, storage.getAll());
    }

    @Test
    void getByIdOne() throws FilmIsNotExistingException, UserIsNotExistingException {
        storage.add(t1);
        assertEquals(t1, storage.getById(1));
    }

    public void shouldThrowExceptionWhenGetByNotExistingIdOne(String s) {
        EntityIsNotExistingException e = assertThrows(
                EntityIsNotExistingException.class,
                () -> storage.getById(1)
        );
        assertEquals(e.getMessage(), s);
    }

    public void shouldThrowExceptionWhenDeleteNotExistingIdOne(String s) {
        EntityIsNotExistingException e = assertThrows(
                EntityIsNotExistingException.class,
                () -> storage.delete(1)
        );
        assertEquals(e.getMessage(), s);
    }
}