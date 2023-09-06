delete from USERS;

--ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;

insert into USERS (ID, LOGIN, NAME, EMAIL, BIRTHDAY) values
(1, 'luke', 'Luke', 'luke@sw.com',  '1595.05.12'),
(2, 'Obi-Wan', 'Obi-Wan Kenobi', 'obi-wan@kenobi.com', '1484.12.2');

ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1;