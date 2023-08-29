DELETE FROM FILMS_LIKE;
DELETE FROM FILM_GENRES;
DELETE FROM FRIENDS;
DELETE FROM USERS;
DELETE FROM FILMS;

ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1;

MERGE INTO MPA_RATING KEY (ID)
    VALUES (1, 'G', 'Нет возрастных ограничений'),
           (2, 'PG', 'Рекомендуется присутствие родителей'),
           (3, 'PG-13', 'Детям до 13 лет просмотр не желателен'),
           (4, 'R', 'Лицам до 17 лет обязательно присутствие взрослого'),
           (5, 'NC-17', 'Лицам до 18 лет просмотр запрещен');

MERGE INTO GENRES KEY(ID)
    VALUES (1,  'Комедия'),
           (2,  'Драма'),
           (3,  'Мультфильм'),
           (4,  'Триллер'),
           (5,  'Документальный'),
           (6,  'Боевик'),
           (7,  'Фантастика'),
           (8,  'Вестерн'),
           (9,  'Детектив'),
           (10, 'Нуар'),
           (11, 'Ужасы'),
           (12, 'Политика'),
           (13, 'Мюзикл'),
           (14, 'Мелодрама'),
           (15, 'Сказка');