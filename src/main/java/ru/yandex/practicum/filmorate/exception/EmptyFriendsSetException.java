package ru.yandex.practicum.filmorate.exception;

public class EmptyFriendsSetException extends RuntimeException {
    public EmptyFriendsSetException(String message) {
        super(message);
    }
}
