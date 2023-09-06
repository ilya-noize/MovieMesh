package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidException;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public final class ErrorController extends Throwable {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, TypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> handleException(TypeMismatchException e) {

        return new ResponseEntity<>(makeMap(e), BAD_REQUEST);
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> handleException(BindException e) {

//        List<String> errors = new ArrayList<>();
//        e.getFieldErrors().forEach(err -> errors.add(err.getField() + ": " + err.getDefaultMessage()));
//        e.getGlobalErrors().forEach(err -> errors.add(err.getObjectName() + ": " + err.getDefaultMessage()));

//        makeMap(e).put("error", errors); // put - Immutable object is modified
        return new ResponseEntity<>(makeMap(e), BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(Throwable e) {

        log.error("[!] Method Argument Not Valid \n Exception:{}", e.getMessage());
        return new ResponseEntity<>(makeMap(e), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistException(UserAlreadyExistException e) {

        log.error("[!] User Already Exist \n Exception:{}", (Object[]) e.getStackTrace());
        return new ResponseEntity<>(makeMap(e), NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidException(ValidException e) {

        log.error("[!] Valid Exception \n Exception:{}", (Object[]) e.getStackTrace());
        return new ResponseEntity<>(makeMap(e), BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException e) {

        log.error("[!] Not Found \n Exception:{}", (Object[]) e.getStackTrace());
        return new ResponseEntity<>(makeMap(e), NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleFriendsException(FriendsException e) {

        log.error("[!] Friends Exception \n Exception:{}", (Object[]) e.getStackTrace());
        return new ResponseEntity<>(makeMap(e), BAD_REQUEST);
    }

    private Map<String, Object> makeMap(Throwable e) {

        return Map.of("timestamp", LocalDate.now().toString(),
                "message", e.getLocalizedMessage(),
                "status", BAD_REQUEST);
    }
}