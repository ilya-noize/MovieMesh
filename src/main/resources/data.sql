--INSERT INTO films (name, description, duration, release, genre_id, mpa_rating_id)
--VALUES ('Star Wars: Episode IV – A New Hope',
-- "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot,
--  a Wookiee and two droids to save the galaxy from the Empire's
--  world-destroying battle station, while also attempting to
--  rescue Princess Leia from the mysterious Darth Vader.",
--  121, 25-05-1977, 1, 1);

DELETE FROM films_likes;
DELETE FROM friends;
DELETE FROM users;
DELETE FROM films;

ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE films ALTER COLUMN id RESTART WITH 1;

MERGE INTO mpa_rating KEY (id)
    VALUES (1, 'G', 'Нет возрастных ограничений'),
           (2, 'PG', 'Рекомендуется присутствие родителей'),
           (3, 'PG-13', 'Детям до 13 лет просмотр не желателен'),
           (4, 'R', 'Лицам до 17 лет обязательно присутствие взрослого'),
           (5, 'NC-17', 'Лицам до 18 лет просмотр запрещен');

MERGE INTO genres KEY(id)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');