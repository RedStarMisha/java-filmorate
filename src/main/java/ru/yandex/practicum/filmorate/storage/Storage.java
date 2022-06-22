package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

public interface Storage <T>{

    T add(T t);

    T update(T t);

    void delete(long id);

    Set<T> getAll();

    T getById(long id);
}
