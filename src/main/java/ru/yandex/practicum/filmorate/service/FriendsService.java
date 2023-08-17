package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.model.Friends;

@Service
public class FriendsService extends MasterService<Friends> {
    public FriendsService(MasterStorageDAO<Friends> storage) {
        super(storage);
    }
}
