package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.storage.StorageUtil.checkUserById;
import static ru.yandex.practicum.filmorate.storage.StorageUtil.userMaker;
@Slf4j
@Component
public class UserStorageDao implements UserStorage{
    private final JdbcTemplate jdbcTemplate;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserStorageDao(JdbcTemplate jdbcTemplate, FriendsStorage friendsStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendsStorage = friendsStorage;
    }

    @Override
    public User add(User user) throws UserAlreadyExistException {
        checkUserOnExist(user.getLogin());
        final User localUser = checkUserName(user);
        String sqlAdd = "INSERT INTO users(user_email, user_login, user_name, user_birthday)\n" +
                        " VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); //Далее получаем ind добавленной сущности
        jdbcTemplate.update(conn -> {
            PreparedStatement ps =conn.prepareStatement(sqlAdd, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, localUser.getEmail());
            ps.setString(2, localUser.getLogin());
            ps.setString(3, localUser.getName());
            ps.setDate(4, Date.valueOf(localUser.getBirthday()));
            return ps;
        },keyHolder);
        return localUser.toBuilder()
                .id(keyHolder.getKey().intValue()).build();
    }

    @Override
    public User update(User user) throws UserIsNotExistingException {
        checkUserById(user.getId(), jdbcTemplate);
        user = checkUserName(user);
        String sqlUpdate = "MERGE INTO USERS USING VALUES (?,?,?,?,?) nu(id, em, lg, nm, bd)\n" +
                "on (USER_ID=nu.id) \n" +
                "when matched then update set USER_EMAIL=nu.em, USER_LOGIN=nu.lg, USER_NAME=nu.nm, USER_BIRTHDAY=nu.bd";
        jdbcTemplate.update(sqlUpdate, user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return user;
    }

    @Override
    public void delete(long id) throws UserIsNotExistingException {
        checkUserById(id, jdbcTemplate);
        String sqlDelete = "DELETE FROM users WHERE user_id=?";
        jdbcTemplate.update(sqlDelete, id);
    }

    @Override
    public User getById(long id) throws UserIsNotExistingException {
        checkUserById(id, jdbcTemplate);
        String sqlGet = "SELECT * FROM USERS WHERE user_id=?";
        return jdbcTemplate.queryForObject(sqlGet, (rs, rowNum) -> userMaker(id, jdbcTemplate), id);
    }

    @Override
    public Set<User> getAll() {
        String sqlGetAll = "SELECT * FROM users";
        List<User> userList = jdbcTemplate.query(sqlGetAll, (rs, rowNum) ->
                userMaker(rs.getInt("user_id"), jdbcTemplate));
        return new HashSet<>(userList);
    }

    private void checkUserOnExist(String login) throws UserAlreadyExistException {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT USER_LOGIN from USERS where USER_LOGIN=?", login);
        if (rowSet.next()) {
            log.warn("user с login - {} уже существует", login);
            throw new UserAlreadyExistException(login);
        }
    }

    private User checkUserName(User user) {
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
