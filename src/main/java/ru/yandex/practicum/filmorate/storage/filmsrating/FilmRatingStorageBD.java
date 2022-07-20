package ru.yandex.practicum.filmorate.storage.filmsrating;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.notexist.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.StorageUtil.*;

@Slf4j
@Component
public class FilmRatingStorageBD implements FilmsRatingStorage{
    private final JdbcTemplate jdbcTemplate;

    private final FilmStorage filmStorage;

    @Autowired
    public FilmRatingStorageBD(JdbcTemplate jdbcTemplate, FilmStorage filmStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
    }

    @Override
    public void addLike(long filmId, long userId) throws UserIsNotExistingException, FilmIsNotExistingException {
        checkUserById(userId, jdbcTemplate);
        String sql = "MERGE INTO FILM_RESPECT_FROM_USER as s USING values (?, ?) r(f, u)\n" +
                "on s.FILM_ID=r.f AND s.USER_ID=r.u\n" +
                "WHEN NOT MATCHED THEN INSERT VALUES (r.f, r.u);";
        jdbcTemplate.update(sql, filmId, userId);
        log.info(String.format("Пользователь с id=%d поставил лайк фильму с id=%d", userId, filmId));
    }

    @Override
    public void deleteLikeFromFilm(long filmId, long userId) throws FilmIsNotExistingException, UserIsNotExistingException {
        String sql = "merge into FILM_RESPECT_FROM_USER as s USING values (?, ?) r(f, u)\n" +
                "on s.FILM_ID=r.f AND s.USER_ID=r.u\n" +
                "when matched then delete;";
        checkUserById(userId, jdbcTemplate);
        checkFilmById(filmId, jdbcTemplate);
        jdbcTemplate.update(sql, filmId, userId);
        log.info(String.format("Пользователь с id=%d убрал свой лайк у фильма с id=%d", userId, filmId));
    }

    @Override
    public List<Film> getPopularFilms(long count) {
        String sql = "select FILM_ID from FILM_RESPECT_FROM_USER\n" +
                "group by FILM_ID\n" +
                "order by count(USER_ID) desc\n" +
                "limit ?";
        List<Film> popularFilms = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("film_id"),count).stream()
                .map((id) -> filmMaker(id, jdbcTemplate)).collect(Collectors.toList());
        List<Film> filmList = jdbcTemplate.query("SELECT film_id FROM FILMS", (rs, rowNum) -> rs.getLong("film_id"))
                .stream().map((id) -> filmMaker(id, jdbcTemplate)).collect(Collectors.toList());
        return popularFilms.isEmpty() ? filmList : popularFilms;
    }
}
