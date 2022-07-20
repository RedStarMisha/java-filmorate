package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.notexist.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmsrating.FilmsRatingStorage;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {

	private final JdbcTemplate jdbcTemplate;
	private final FilmStorage filmStorage;
	private final FilmsRatingStorage ratingStorage;
	private final FriendsStorage friendsStorage;
	private final GenreStorage genreStorage;
	private final MpaStorage mpaStorage;
	private final UserStorage userStorage;

	private User testUser1;
	private User testUser2;
	private User testUser3;
	private Film testFilm1;
	private Film testFilm2;
	private Film testFilm3;


	@AfterEach
	void dropData() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "users", "films");
	}

	@BeforeEach
	void createData() {
		testUser1 = User.builder()
				.email("mail@gmail.ru")
				.login("emmy")
				.name("gag")
				.birthday(LocalDate.of(1986, 02, 20))
				.build();
		testUser2 = User.builder()
				.email("mail@ya.ru")
				.login("Kate")
				.name("KATO15")
				.birthday(LocalDate.of(1999, 02, 20))
				.build();
		testUser3 = User.builder()
				.email("mail@mail.ru")
				.login("dolore")
				.name("Nick Name")
				.birthday(LocalDate.of(1946, 02, 20))
				.build();
		testFilm1 =  Film.builder()
				.name("labore nulla")
				.description("Duis in consequat esse")
				.duration(100)
				.releaseDate(LocalDate.of(1979,04,17))
				.genres(List.of(new Genre(1, "Комедия")))
				.mpa(new Rating(5, "NC-17"))
				.build();
		testFilm2 =  Film.builder()
				.name("Green mile")
				.description("The cinema about black man")
				.duration(120)
				.releaseDate(LocalDate.of(1993,04,17))
				.genres(List.of(new Genre(2, "Драма")))
				.mpa(new Rating(5, "NC-17"))
				.build();
		testFilm3 =  Film.builder()
				.name("Терминатор 2")
				.description("Rise of the Machines")
				.duration(120)
				.releaseDate(LocalDate.of(1991,04,17))
				.genres(List.of(new Genre(6, "Боевик")))
				.mpa(new Rating(5, "NC-17"))
				.build();
	}



	@Test
	void shouldGetGenreWithIdTwo() throws GenreIsNotExistingException {
		Genre genre = new Genre(2, "Драма");
		Genre testGenre = genreStorage.getById(2);
		assertEquals(genre, testGenre);
	}

	@Test
	void shouldThrowExceptionWhenIncorrectGenreId() {
		GenreIsNotExistingException e = assertThrows(GenreIsNotExistingException.class,
				()->genreStorage.getById(-1));
		assertEquals(e.getMessage(), "Такого жанра нет");
	}

	@Test
	void shouldGetAllGenre() {
		List<Genre> testGenreList = genreStorage.getAll();
		assertThat(testGenreList, containsInAnyOrder(
				new Genre(1, "Комедия"),
				new Genre(2, "Драма"),
				new Genre(3, "Мультфильм"),
				new Genre(4, "Триллер"),
				new Genre(5, "Документальный"),
				new Genre(6, "Боевик")));
	}
	@Test
	void shouldGetMpaByIdThree() throws MPAIsNotExistingException {
		assertEquals(new Rating(3, "PG-13"), mpaStorage.getMpaById(3));
	}

	@Test
	void shouldThrowExceptionWhenIncorrectMpaId() {
		MPAIsNotExistingException e = assertThrows(MPAIsNotExistingException.class,
				() -> mpaStorage.getMpaById(-5));
		assertEquals(e.getMessage(), "Такого рейтинга нет");
	}

	@Test
	void shouldGetAllMpaRatings() {
		assertThat(mpaStorage.getAll(), containsInAnyOrder(
				new Rating( 1, "G" ),
				new Rating(2, "PG"),
				new Rating(3, "PG-13"),
				new Rating(4, "R"),
				new Rating(5, "NC-17")));
	}

	@Test
	void shouldGetUserByIdOne() throws UserAlreadyExistException, UserIsNotExistingException {
		userStorage.add(testUser1);
		testUser1.setId(1);
		assertEquals(1, userStorage.getAll().size());
		assertEquals(userStorage.getById(1), testUser1);
	}
	@Test
	void shouldThrowExceptionWhenAddedExistingUser() throws UserAlreadyExistException {
		userStorage.add(testUser1);
		UserAlreadyExistException e = assertThrows(UserAlreadyExistException.class,
				() -> userStorage.add(testUser1));
		assertEquals(e.getMessage(), String.format("Пользователь с login %s уже существует", testUser1.getLogin()));
	}

	@Test
	void shouldUpdateUserWithEmptyNameField() throws UserAlreadyExistException, UserIsNotExistingException {
		userStorage.add(testUser1);
		testUser1 = testUser1.toBuilder()
				.id(1)
				.name("")
				.login("Jane").build();
		userStorage.update(testUser1);
		testUser1.setName("Jane");
		assertEquals(testUser1, userStorage.getById(1));
	}
	@Test
	void shouldThrowExceptionWhenUpdateUserWithIncorrectId() {
		testUser1.setId(30);
		UserIsNotExistingException e = assertThrows(UserIsNotExistingException.class,
				() -> userStorage.update(testUser1));
		assertEquals(e.getMessage(), "Пользователя c id = 30 не существует");
	}

	@Test
	void shouldDeleteUserById() throws UserAlreadyExistException, UserIsNotExistingException {
		userStorage.add(testUser1);
		userStorage.delete(1);
		assertEquals(0, userStorage.getAll().size());
	}
	@Test
	void shouldThrowExceptionWhenDeleteUserWithIncorrectId() {
		UserIsNotExistingException e = assertThrows(UserIsNotExistingException.class,
				() -> userStorage.delete(50));
		assertEquals(e.getMessage(), "Пользователя c id = 50 не существует");
	}

	@Test
	void shouldThrowExceptionWhenGetUserWithId() {
		UserIsNotExistingException e = assertThrows(UserIsNotExistingException.class,
				() -> userStorage.getById(100));
		assertEquals(e.getMessage(), "Пользователя c id = 100 не существует");
	}

	@Test
	void shouldGetAllUsers() throws UserAlreadyExistException {
		userStorage.add(testUser1);
		userStorage.add(testUser2);
		userStorage.add(testUser3);
		testUser1.setId(1);
		testUser2.setId(2);
		testUser3.setId(3);
		assertThat(userStorage.getAll(), containsInAnyOrder(testUser1, testUser2, testUser3));
	}

	@Test
	void shouldGetEmptyListOfUser() {
		assertTrue(userStorage.getAll().isEmpty());
	}

	@Test
	void shouldAddFriendship() throws UserAlreadyExistException, UserIsNotExistingException {
		userStorage.add(testUser1);
		userStorage.add(testUser2);
		userStorage.add(testUser3);
		friendsStorage.add(1, 2);
		testUser2.setId(2);
		assertEquals(1, friendsStorage.getFriends(1).size());
		assertThat(friendsStorage.getFriends(1), containsInAnyOrder(testUser2));
	}

	@Test
	void shouldThrowExceptionThenAddFriendshipBetweenUsersWithIncorrectId() throws UserAlreadyExistException {
		userStorage.add(testUser1);
		UserIsNotExistingException e1 = assertThrows(UserIsNotExistingException.class,
				() -> friendsStorage.add(1, 10));
		assertEquals(e1.getMessage(), "Пользователя c id = 10 не существует");
		UserIsNotExistingException e2 = assertThrows(UserIsNotExistingException.class,
				() -> friendsStorage.add(10, 1));
		assertEquals(e2.getMessage(), "Пользователя c id = 10 не существует");
	}

	@Test
	void shouldGetCommonFriends() throws UserAlreadyExistException, UserIsNotExistingException {
		userStorage.add(testUser1);
		userStorage.add(testUser2);
		userStorage.add(testUser3);
		friendsStorage.add(1, 3);
		friendsStorage.add(2, 3);
		testUser3.setId(3);
		assertEquals(1, friendsStorage.getCommonFriends(1, 2).size());
		assertThat(friendsStorage.getCommonFriends(1, 2), containsInAnyOrder(testUser3));
	}

	@Test
	void shouldAddNewFilm() throws GenreIsNotExistingException, MPAIsNotExistingException, FilmIsNotExistingException {
		filmStorage.add(testFilm1);
		testFilm1.setId(1);
		assertEquals(testFilm1, filmStorage.getById(1));
	}

	@Test
	void shouldUpdateFilm() throws FilmIsNotExistingException, GenreIsNotExistingException, MPAIsNotExistingException {
		filmStorage.add(testFilm1);
		testFilm1.setDescription("superFilm");
		testFilm1.setGenres(List.of(new Genre(1, "Комедия"), new Genre(6, "Боевик")));
		testFilm1.setId(1);
		testFilm1.setMpa(new Rating(4, "R"));
		filmStorage.update(testFilm1);
		assertEquals(testFilm1, filmStorage.getById(1));
	}

	@Test
	void shouldDeleteFilmById() throws GenreIsNotExistingException, MPAIsNotExistingException, FilmIsNotExistingException {
		filmStorage.add(testFilm1);
		filmStorage.delete(1);
		assertEquals(0, filmStorage.getAll().size());
	}

	@Test
	void shouldGetAllFilms() throws GenreIsNotExistingException, MPAIsNotExistingException {
		filmStorage.add(testFilm1);
		filmStorage.add(testFilm1);
		filmStorage.add(testFilm3);
		testFilm1.setId(1);
		testFilm2.setId(2);
		testFilm3.setId(3);
		assertThat(filmStorage.getAll(), containsInAnyOrder(testFilm1, testFilm2, testFilm3));
	}

	@Test
	void shouldGetEmptyFilmsList() {
		assertEquals(filmStorage.getAll().size(), 0);
	}

	@Test
	void shouldAddLikeToFilm() throws EntityIsNotExistingException, UserAlreadyExistException {
		userStorage.add(testUser1);
		filmStorage.add(testFilm1);
		ratingStorage.addLike(1, 1);
		assertEquals(filmStorage.getById(1).getIdUserWhoLikedSet().size(), 1);
		assertTrue(filmStorage.getById(1).getIdUserWhoLikedSet().contains(1L));
	}

	@Test
	void shouldDeleteLikeFromFilm() throws EntityIsNotExistingException, UserAlreadyExistException {
		userStorage.add(testUser1);
		filmStorage.add(testFilm1);
		ratingStorage.addLike(1, 1);
		ratingStorage.deleteLikeFromFilm(1, 1);
		assertEquals(filmStorage.getById(1).getIdUserWhoLikedSet().size(), 0);
	}

	@Test
	void shouldGetPopularFilm() throws FilmIsNotExistingException, UserIsNotExistingException, UserAlreadyExistException, GenreIsNotExistingException, MPAIsNotExistingException {
		filmStorage.add(testFilm1);
		filmStorage.add(testFilm1);
		filmStorage.add(testFilm3);
		userStorage.add(testUser1);
		userStorage.add(testUser2);
		userStorage.add(testUser3);
		testFilm1.setId(1);
		testFilm2.setId(2);
		testFilm3.setId(3);
		ratingStorage.addLike(1, 1);
		ratingStorage.addLike(1, 2);
		ratingStorage.addLike(1, 3);
		ratingStorage.addLike(2, 1);
		ratingStorage.addLike(3, 2);
		ratingStorage.addLike(3, 3);
		assertEquals(List.of(1L, 3L, 2L), List.of(testFilm1.getId(), testFilm3.getId(), testFilm2.getId()));
	}
}
