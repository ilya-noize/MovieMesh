package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FailSetFriendException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MasterStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserFriendsService extends UserService {
    @Autowired
    protected UserFriendsService(MasterStorage<User> userStorage) {
        super((UserStorage) userStorage);
    }

    /**
     * добавление в друзья
     *
     * @param id       уин пользователя
     * @param idFriend уин пользователя-друга
     */
    @Override
    public void addFriend(Long id, Long idFriend) {
        if (this.get(id) != null && this.get(idFriend) != null) {
            addFriendByOne(id, idFriend);
            addFriendByOne(idFriend, id);
        }
    }

    /**
     * удаление из друзей.
     *
     * @param supposedId       уин пользователя
     * @param supposedIdFriend уин пользователя-друга
     */
    @Override
    public void deleteFriend(Long supposedId, Long supposedIdFriend) {
        User user = this.get(supposedId);
        Long userId = user.getId();
        Set<Long> userFriends = user.getFriends();

        User userFriend = this.get(supposedIdFriend);
        Long userFriendId = userFriend.getId();
        Set<Long> userFriendFriends = userFriend.getFriends();

        log.info("* Удаление из друзей {}< - >{}", user.getLogin(), userFriend.getLogin());

        if (userFriends.remove(userFriendId) && userFriendFriends.remove(userId)) {
            user.setFriends(userFriends);
            userFriend.setFriends(userFriendFriends);
        } else {
            String error = "Неудачное удаление из списка друзей";
            log.error(error);
            throw new FailSetFriendException(error);
        }
        log.info("* Удаление завершено {}< o >{}", user.getId(), userFriend.getId());
    }

    /**
     * возвращает список пользователей, являющихся друзьями пользователя.
     *
     * @param supposedId уин пользователя
     * @return список
     */
    @Override
    public Set<User> getFriends(Long supposedId) {
        User user = get(supposedId);

        log.info("* Возвращаем список пользователей, являющихся друзьями пользователя {}", user.getLogin());
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
    @Override
    public Set<User> getFriendsCommon(Long id, Long otherId) {
        User user = get(id);
        User userOther = get(otherId);

        log.info("* Возвращаем список общих друзей между пользователями {}<->{}", user.getLogin(), userOther.getLogin());
        Set<Long> friendsIdUser = user.getFriends();
        Set<Long> friendsIdUserOther = userOther.getFriends();
        Set<Long> friendsCommon = findFriendsCommon(friendsIdUser, friendsIdUserOther);

        return getFriendsSet(friendsCommon);
    }

    private void addFriendByOne(Long id, Long idFriend) {
        Set<Long> userFriends = this.get(id).getFriends();
        if (userFriends.add(idFriend)) {
            this.get(id).setFriends(userFriends);
        } else {
            String error = "Неудачное добавление в список друзей";
            log.error(error);
            throw new FailSetFriendException(error);
        }
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
                .collect(Collectors.toSet());
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
        log.info("* Возвращаем список пользователей-друзей");
        Set<User> friendsSet = friendsIdSet.stream()
                .map(this::get)
                .collect(Collectors.toSet());
        if (friendsSet.size() == 0) {
            return new HashSet<>();
        }
        return friendsSet;
    }
}
