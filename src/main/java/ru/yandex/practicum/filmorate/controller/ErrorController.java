package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;

import javax.validation.ValidationException;
import java.time.ZonedDateTime;
import java.util.Map;

import static java.time.Clock.system;
import static java.time.Clock.systemDefaultZone;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public final class ErrorController extends RuntimeException {
    private static final ZonedDateTime NOW = ZonedDateTime.now(system(systemDefaultZone().getZone()));

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, TypeMismatchException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<?> handleException(Exception e) {

        log.error("[!] Exception:\n {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .lastModified(NOW)
                .header("error", e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<?> handleUserAlreadyExistException(UserAlreadyExistException e) {

        log.error("[!] User Already Exist \n Exception:{}", (Object[]) e.getStackTrace());
        return ResponseEntity
                .badRequest()
                .lastModified(NOW)
                .header("error", e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<?> handleValidException(ValidationException e) {

        log.error("[!] Valid Exception \n Exception:{}", (Object[]) e.getStackTrace());
        return ResponseEntity
                .badRequest()
                .lastModified(NOW)
                .header("error", e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(Map.of(
                "timestamp", NOW,
                "error", e.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler(FriendsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<?> handleFriendsException(FriendsException e) {

        log.error("[!] Friends Exception \n Exception:{}", (Object[]) e.getStackTrace());
        return ResponseEntity
                .badRequest()
                .lastModified(NOW)
                .header("error", e.getLocalizedMessage())
                .build();
    }
}