package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)// todo not used
    public ErrorResponse handleEmptyFriendsSetException(EmptyFriendsSetException e) {
        log.error("{}", (Object) e.getStackTrace());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserAlreadyExistException(UserAlreadyExistException e) {
        log.error("{}", (Object) e.getStackTrace());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        log.error("{}", (Object) e.getStackTrace());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWrongUserIdException(WrongUserIdException e) {
        log.error("{}", (Object) e.getStackTrace());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWrongFilmIdException(WrongFilmIdException e) {
        log.error("{}", (Object) e.getStackTrace());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler//(Throwable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFailSetFriendException(FailSetFriendException e) {
        log.error("{}", (Object) e.getStackTrace());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler//(Throwable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFailSetFilmLikesException(FailSetFilmLikesException e) {
        log.error("{}", (Object) e.getStackTrace());
        return new ErrorResponse(e.getMessage());
    }
}