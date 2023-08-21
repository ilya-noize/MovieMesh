package ru.yandex.practicum.filmorate.exception;

public class FriendsException extends RuntimeException {
    public FriendsException(String message) {
        super(message);
    }

    public FriendsException() {
        super("Which is much more valuable than your friendship with yourself.");
    }
}
