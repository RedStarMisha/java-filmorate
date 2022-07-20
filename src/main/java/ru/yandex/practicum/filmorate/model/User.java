package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.exceptions.UsersIsNotFriendsException;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
@EqualsAndHashCode
@Getter
@Setter
@Builder(toBuilder = true)
public class User {

    @Max(Long.MAX_VALUE)
    private long id;

    @Email
    private String email;

    @NotEmpty
    @Pattern(message = "Не может содержать пробел",regexp = "\\w+")
    private String login;

    private String name;

    @NotNull
    @Past
    private LocalDate birthday;

    private Set<Long> friendsId;

    public void deleteFriend(long friendId) {
    }

    public void addFriend(long friendId) {
    }
}
