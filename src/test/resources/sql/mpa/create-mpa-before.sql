DELETE FROM MPA_RATING;

alter table MPA_RATING alter COLUMN ID RESTART with 1;

INSERT INTO MPA_RATING(ID, NAME, DESCRIPTION) VALUES
(1, 'G', 'Нет возрастных ограничений'),
(2, 'PG', 'Рекомендуется присутствие родителей'),
(3, 'PG-13', 'Детям до 13 лет просмотр не желателен'),
(4, 'R', 'Лицам до 17 лет обязательно присутствие взрослого'),
(5, 'NC-17', 'Лицам до 18 лет просмотр запрещен');
