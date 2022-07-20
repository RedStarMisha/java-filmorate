package ru.yandex.practicum.filmorate.storage.friends;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.storage.StorageUtil.checkUserById;
import static ru.yandex.practicum.filmorate.storage.StorageUtil.userMaker;

@Slf4j
@Component
public class FriendsStorageDao implements FriendsStorage{
    private final JdbcTemplate jdbcTemplate;
        private final String SQL_UPDATE_FRIENDSHIP = "MERGE INTO FRIENDS as fs\n" +
                " USING (select user_id, friends_id, friends_confirmed from FRIENDS WHERE user_id = ? AND friends_id = ?) as fr\n" +
                " ON (fs.USER_ID=fr.user_id AND fs.FRIENDS_ID=fr.friends_id)\n" +
                " WHEN MATCHED then update set fs.FRIENDS_CONFIRMED=?\n";
        private

    @Autowired
    FriendsStorageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(long userId, long friendId) throws UserIsNotExistingException {
        checkUserById(userId, jdbcTemplate);
        checkUserById(friendId, jdbcTemplate);
        int updated = checkFriendshipStatus(userId, friendId);
        String sqlMergeIntoFriends = "MERGE into FRIENDS using values (?,?,'NO') ref(u,f,s)\n" +
                "                on (USER_ID=ref.u and FRIENDS_ID=ref.f)\n";
        if (updated > 0) {
            String sqlIfAreFriends = sqlMergeIntoFriends +
                    "when matched and FRIENDS_CONFIRMED=ref.s then update set FRIENDS_CONFIRMED='YES'\n" +
                    "when not matched then insert values (ref.u, ref.f, 'YES')";
            jdbcTemplate.update(sqlIfAreFriends);
            return;
        }
        String sqlIfAreNotFriends = sqlMergeIntoFriends + "when not matched then insert values (ref.u, ref.f, 'NO')";
        jdbcTemplate.update(sqlIfAreNotFriends, userId, friendId);
    }

    private int checkFriendshipStatus(long userId, long friendId) {
        String sqlMergeFriendship = "MERGE into FRIENDS using values (?,?,'NO') ref(u,f,s)\n" +
                "                on (USER_ID=ref.u and FRIENDS_ID=ref.f)\n" +
                "when matched and FRIENDS_CONFIRMED=ref.s then update set FRIENDS_CONFIRMED='YES'";
        return jdbcTemplate.update(sqlMergeFriendship, friendId, userId);
    }

    @Override
    public Set<User> getFriends(long userId) throws UserIsNotExistingException {
        checkUserById(userId, jdbcTemplate);
        String sqlGetFriends = "SELECT friends_id FROM friends WHERE user_id=?";
        List<User> friendsList = jdbcTemplate.query(sqlGetFriends, (rs, rowNum) ->
                userMaker(rs.getInt("friends_id"), jdbcTemplate), userId);
        return new HashSet<>(friendsList);
    }

    @Override
    public Set<User> getCommonFriends(long userId, long otherUserId) throws UserIsNotExistingException {
        checkUserById(userId, jdbcTemplate);
        checkUserById(otherUserId, jdbcTemplate);
        String sqlGetCommonFriends = "select f1.FRIENDS_ID from FRIENDS as f1\n" +
                "                  join FRIENDS as f2 on f1.FRIENDS_ID=f2.FRIENDS_ID\n" +
                "                  where f1.USER_ID=? and f2.USER_ID=?";
        List<User> commonFriends = jdbcTemplate.query(sqlGetCommonFriends, (rs, rowNum) ->
                userMaker(rs.getInt("friends_id"), jdbcTemplate), userId, otherUserId);
        return new HashSet<>(commonFriends);
    }

    @Override
    public void deleteFriend(long userId, long friendId) throws UserIsNotExistingException {
        String sqlMergeIntoFriends = "MERGE into FRIENDS using values (?,?,'YES') ref(u,f,s)\n" +
                "on (USER_ID=ref.u and FRIENDS_ID=ref.f)\n";
        String sqlCheckFriendFriendship = sqlMergeIntoFriends +
                "when matched and FRIENDS_CONFIRMED=ref.s then update set FRIENDS_CONFIRMED='NO'";
        jdbcTemplate.update(sqlCheckFriendFriendship, friendId, userId);
        String sqlCheckUserFriendship = sqlMergeIntoFriends +
                "when matched then delete";
        jdbcTemplate.update(sqlCheckUserFriendship, userId, friendId);
    }
}
