package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
public class UserService extends MasterService<User> {
    private final MasterService<Friends> friends;

    @Autowired
    protected UserService(MasterStorageDAO<User> userStorage, MasterService<Friends> friends) {
        super(userStorage);
        this.friends = friends;
    }

    /**
     * создание пользователя
     *
     * @param user пользователь
     * @return пользователь
     */
    @Override
    public User create(User user) {
        return super.create(valid(user));
    }

    /**
     * обновление пользователя
     *
     * @param user пользователь
     * @return пользователь
     */
    @Override
    public User update(User user) {
        return super.update(valid(user));
    }


    public void addFriend(Long id, Long friendId) {
        isExist(id);
        isExist(friendId);
        friends.create(new Friends(id, friendId));
    }

    public void deleteFriend(Long id, Long friendId) {
        isExist(id);
        isExist(friendId);
        friends.delete(id, friendId);
    }

    /**
     * возвращает список пользователей, являющихся друзьями пользователя.
     *
     * @param supposedId уин пользователя
     * @return список
     */
    public Set<User> getFriends(Long supposedId) {
        User user = get(supposedId);

        log.info("Returning the user's friends list {}", user.getLogin());
        Set<Long> friendsIdUser = user.getFriends();

        return getFriendsSet(friendsIdUser);
    }

    /**
     * возвращает список общих друзей между пользователями
     *
     * @param id      уин пользователя №1
     * @param otherId уин пользователя №2
     * @return список
     */
    public Set<User> getFriendsCommon(Long id, Long otherId) {
        User user = get(id);
        User userOther = get(otherId);
        if (user.equals(userOther)) {
            throw new FriendsException();
        }

        log.info("Returning a list of friends between users {}<->{}", user.getLogin(), userOther.getLogin());

        Set<Long> friendsIdUser = user.getFriends();
        Set<Long> friendsIdUserOther = userOther.getFriends();
        Set<Long> friendsCommon = findFriendsCommon(friendsIdUser, friendsIdUserOther);

        return getFriendsSet(friendsCommon);
    }

    /**
     * Поиск общих чисел в HashSet-ах. А именно,
     * аналог HashSet1<T>.retainAll(HashSet2<T>), но (!)
     * без затирания HashSet1
     *
     * @param friendsIdUser      HashSet1
     * @param friendsIdUserOther HashSet2
     * @return пересечение HashSet1 и HashSet2 - другой HashSet3
     */
    private Set<Long> findFriendsCommon(Set<Long> friendsIdUser, Set<Long> friendsIdUserOther) {
        return friendsIdUser.stream()
                .filter(friendsIdUserOther::contains)
                .collect(toSet());
    }

    /**
     * возвращает список пользователей-друзей
     *
     * @param friendsIdSet список уин пользователей
     * @return список
     * @see #getFriends
     * @see #getAll()
     */
    private Set<User> getFriendsSet(Set<Long> friendsIdSet) {
        log.info("Get friends.");
        Set<User> friendsSet = friendsIdSet.stream()
                .map(this::get)
                .collect(toSet());
        if (friendsSet.size() == 0) {
            return new HashSet<>();
        }
        return friendsSet;
    }

    /**
     * Валидация данных о пользователе
     *
     * @param user пользователь
     * @return пользователь
     */
    private User valid(User user) {
        user.setLogin(loginUnical(user));
        user.setName(checkName(user));
        user.setBirthday(checkBirthday(user));
        user.setEmail(checkEmail(user));
        return user;
    }

    /**
     * Проверка логина на уникальность
     *
     * @param user пользователь
     */
    @Pattern(regexp = "^\\S*$", message = "The login cannot contain spaces.")
    @Size(min = 3, max = 20, message = "The login must be from 3 to 20 characters.")
    private String loginUnical(User user) {
        log.info("[?] unical login.");
        String login = user.getLogin();

        final Predicate<User> TWIN_LOGIN = user1 -> user1.getLogin().equals(login);
        Optional<User> result = getAll().stream().filter(TWIN_LOGIN).findFirst();

        if (result.isPresent()) {
            String error = String.format("User with login:%s exist.", login);
            log.error(error);
            throw new UserAlreadyExistException(error);
        }
        log.info("Login correct.");
        return login;
    }

    /**
     * Проверка имени пользователя
     *
     * @param user пользователь
     * @return имя пользователя
     */
    private String checkName(User user) {
        log.info("[?] Username.");
        String name = user.getName();
        if (name == null || name.isBlank()) {
            log.info("Username is login.");
            return user.getLogin();
        } else {
            log.info("Username correct.");
            return name;
        }
    }

    @Past(message = "The date of birth should only be in the past.")
    private LocalDate checkBirthday(User user) {
        log.info("[?] Birthday");
        return user.getBirthday();
    }

    @Email(message = "The entered value is not an email address.")
    private String checkEmail(User user) {
        log.info("[?] Email");
        return user.getEmail();
    }
}