package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.notexist.EntityIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.GenreIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.MPAIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.StorageUtil.checkFilmById;
import static ru.yandex.practicum.filmorate.storage.StorageUtil.filmMaker;

@Slf4j
@Component
@AllArgsConstructor
public class FilmStorageDao implements FilmStorage{

    private final MpaStorage mpaStorage;
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Film add(Film film) throws MPAIsNotExistingException, GenreIsNotExistingException {
        String sqlFilmAdd = "INSERT INTO FILMS (FILM_NAME, FILM_DESCRIPTION, FILM_DURATION, FILM_DATE, RATING_ID)\n" +
                        "VALUES(?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); //Далее получаем ind добавленной сущности
        jdbcTemplate.update(conn -> {
            PreparedStatement ps =conn.prepareStatement(sqlFilmAdd, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setLong(3, film.getDuration());
            ps.setDate(4, Date.valueOf(film.getReleaseDate()));
            ps.setInt(5, film.getMpa().getId());
            return ps;
        },keyHolder);
        Integer filmId = keyHolder.getKey().intValue();
        List<Genre> genres = addFilmGenreToBase(film.getGenres(), filmId);
        addFilmGenreToBase(film.getGenres(), filmId);
        return film.toBuilder()
                .id(filmId)
                .mpa(mpaStorage.getMpaById(film.getMpa().getId()))
                .genres(genres)
                .build();
    }

    @Override
    public Film update(Film film) throws FilmIsNotExistingException, GenreIsNotExistingException {
        delete(film.getId());
        String sqlAdd = "INSERT INTO FILMS (FILM_ID, FILM_NAME, FILM_DESCRIPTION, FILM_DURATION, FILM_DATE, RATING_ID)\n" +
                        "VALUES(?,?,?,?,?,?)";
        jdbcTemplate.update(sqlAdd, film.getId(), film.getName(), film.getDescription(), film.getDuration(),
                film.getReleaseDate(), film.getMpa().getId());
        List<Genre> genres = addFilmGenreToBase(film.getGenres(), film.getId());
        addFilmGenreToBase(film.getGenres(), film.getId());
        return film.toBuilder()
                .genres(genres)
                .build();
    }

    @Override
    public void delete(long id) throws FilmIsNotExistingException {
        checkFilmById(id, jdbcTemplate);
        String sqlDelete = "DELETE FROM films WHERE film_id=?";
        jdbcTemplate.update(sqlDelete, id);
    }

    @Override
    public Set<Film> getAll() {
        return jdbcTemplate.query("SELECT film_id FROM FILMS", (rs, rowNum) ->  rs.getLong("film_id"))
                .stream().map(id -> filmMaker(id, jdbcTemplate)).collect(Collectors.toSet());
    }

    @Override
    public Film getById(long id) throws FilmIsNotExistingException {
        checkFilmById(id, jdbcTemplate);
        return filmMaker(id, jdbcTemplate);
    }

    private List<Genre> addFilmGenreToBase(List<Genre> genres, long filmId) throws GenreIsNotExistingException {
        if (genres == null) {
            return null;
        }
        String sqlAddGenreToFilm = "merge into FILM_GENRE using values (?,?) ref(f,g)\n" +
                        "    on FILM_ID=ref.f and GENRE_ID=ref.g\n" +
                        "    when not matched then insert values (ref.f, ref.g);";
        for (Genre genre:genres) {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE WHERE GENRE_ID=?", genre.getId());
            if (rowSet.next()) {
                jdbcTemplate.update(sqlAddGenreToFilm, filmId, genre.getId());
                continue;
            }
            log.warn("Жанра с id = {} не существует", genre.getId());
            throw new GenreIsNotExistingException(String.format("Жанра с id = %d не существует", genre.getId()));
        }
        return makeGenre(filmId);
    }

        private List<Genre> makeGenre(long film_id) {
        String sqlGenre = "SELECT g.* FROM GENRE as G LEFT JOIN FILM_GENRE FG on G.GENRE_ID = FG.GENRE_ID\n" +
                "WHERE FILM_ID=?";
        return jdbcTemplate.query(sqlGenre, (rs, rowNum) ->
                        new Genre(rs.getInt("genre_id"), rs.getString("genre_name"))
                , film_id);
    }
}
