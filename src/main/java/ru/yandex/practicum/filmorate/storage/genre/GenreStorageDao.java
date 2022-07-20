package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.notexist.GenreIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
@Slf4j
@Component
public class GenreStorageDao implements GenreStorage{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreStorageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getById(int id) throws GenreIsNotExistingException {
        String sql = "Select * From GENRE Where GENRE_ID=?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            return new Genre(id, rowSet.getString("genre_name"));
        }
        log.warn("genre с id = {} не существует", id);
        throw new GenreIsNotExistingException("Такого жанра нет");
    }

    @Override
    public List<Genre> getAll() {
        String sql = "Select * From GENRE";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
    }
}
