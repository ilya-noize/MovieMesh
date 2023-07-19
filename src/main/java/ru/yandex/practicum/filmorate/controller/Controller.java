package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.FilmorateApplication;

public abstract class Controller {
    protected final Logger log = LoggerFactory.getLogger(FilmorateApplication.class);
}
