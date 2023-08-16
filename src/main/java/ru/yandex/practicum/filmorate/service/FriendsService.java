package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

@Service
public class FriendsService extends MasterService<Friends> {
    public FriendsService(MasterStorage<Friends> storage) {
        super(storage);
    }
}
