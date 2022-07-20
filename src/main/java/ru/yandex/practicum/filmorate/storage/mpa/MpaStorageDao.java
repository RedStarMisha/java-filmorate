package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.notexist.MPAIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
@Slf4j
@Component
public class MpaStorageDao implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaStorageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating getMpaById(Integer id) throws MPAIsNotExistingException {
        String sql = "Select * From rating Where RATING_ID=?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            return new Rating(rowSet.getInt("RATING_ID"), rowSet.getString("RATING_DESCRIPTION"));
        }
        log.warn("mpa с id = {} не существует", id);
        throw new MPAIsNotExistingException("Такого рейтинга нет");
    }


    @Override
    public List<Rating> getAll() {
        String sql = "Select * From rating";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Rating(rs.getInt("RATING_ID"), rs.getString("RATING_DESCRIPTION")));
    }
}
