package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.exceptions.notexist.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
@Slf4j
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
    public static Film filmMaker(long id, JdbcTemplate jdbcTemplate) {
        String sqlGetLikes = "SELECT * FROM FILM_RESPECT_FROM_USER where FILM_ID=?";
        List<Long> likes = jdbcTemplate.query(sqlGetLikes, (rs, rowNum) -> rs.getLong("user_id"), id);
        String sqlGenre = "SELECT g.* FROM GENRE as G LEFT JOIN FILM_GENRE FG on G.GENRE_ID = FG.GENRE_ID\n" +
                "WHERE FILM_ID=?";
        List<Genre> genres = jdbcTemplate.query(sqlGenre, (rs, rowNum) ->
                        new Genre(rs.getInt("genre_id"), rs.getString("genre_name"))
                , id);
        String sqlGetFilmData = "SELECT films.*, r.RATING_DESCRIPTION FROM FILMS join RATING R on R.RATING_ID = FILMS.RATING_ID " +
                "WHERE films.FILM_ID=?";
        return jdbcTemplate.queryForObject(sqlGetFilmData, (rs, rowNum) -> Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_description"))
                .duration(rs.getInt("film_duration"))
                .releaseDate(rs.getDate("film_date").toLocalDate())
                .genres(genres)
                .mpa(new Rating(rs.getInt("rating_id"), rs.getString("RATING_DESCRIPTION")))
                .idUserWhoLikedSet(likes)
                .build()
        , id);
    }

    public static void checkUserById(long id, JdbcTemplate jdbcTemplate) throws UserIsNotExistingException {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID from USERS where USER_ID=?", id);
        if (!rowSet.next()) {
            log.warn("user с id = {} не существует", id);
            throw new UserIsNotExistingException(String.format("Пользователя c id = %d не существует", id));
        }
    }

    public static void checkFilmById(long id, JdbcTemplate jdbcTemplate) throws FilmIsNotExistingException {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT FILM_ID from FILMS where FILM_ID=?", id);
        if (!rowSet.next()) {
            log.warn("film с id = {} не существует", id);
            throw new FilmIsNotExistingException(String.format("Фильм с id=%d не найден", id));
        }
    }
}
