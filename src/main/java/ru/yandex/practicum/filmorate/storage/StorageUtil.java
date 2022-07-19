package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.exceptions.notexist.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.storage.mpa.model.User;

public class StorageUtil {

    public static User userMaker(long userId, JdbcTemplate jdbcTemplate) {
        String sqlGetUser = "SELECT * FROM users WHERE user_id=?";
        return jdbcTemplate.queryForObject(sqlGetUser, (rs, rowNum) ->
                User.builder()
                        .id(rs.getInt("user_id"))
                        .email(rs.getString("user_email"))
                        .login(rs.getString("user_login"))
                        .name(rs.getString("user_name"))
                        .birthday(rs.getDate("user_birthday").toLocalDate())
                        .build(), userId);
    }

    public static void checkUserById(long id, JdbcTemplate jdbcTemplate) throws UserIsNotExistingException {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID from USERS where USER_ID=?", id);
        if (!rowSet.next()) {
            throw new UserIsNotExistingException(String.format("Пользователя c id = %d не существует", id));
        }
    }

    public static void checkFilmById(long id, JdbcTemplate jdbcTemplate) throws FilmIsNotExistingException {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT FILM_ID from FILMS where FILM_ID=?", id);
        if (!rowSet.next()) {
            throw new FilmIsNotExistingException(String.format("Фильм с id=%d не найден", id));
        }
    }
}
