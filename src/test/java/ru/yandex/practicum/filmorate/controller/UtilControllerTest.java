package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;

public abstract class UtilControllerTest<T> {
    Controller<T> controller;
    Gson gson = new Gson();
    T element;
}
