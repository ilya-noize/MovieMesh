package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.model.Friends;

@Service
public class FriendsService extends MasterService<Friends> {
    public FriendsService(MasterStorageDAO<Friends> storage) {
        super(storage);
    }

    @Override
    public Friends create(Friends friends) {
        if (friends.getRequestId().equals(friends.getFriendId())) {
            throw new FriendsException();
        }
        return super.create(friends);
    }

    @Override
    public void delete(Long... id) {
        Friends friends = new Friends(id[0], id[1]);
        if (friends.getRequestId().equals(friends.getFriendId())) {
            throw new FriendsException();
        }
        super.delete(id);
    }
}
