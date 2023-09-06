DELETE FROM USERS;

--ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;

INSERT INTO USERS (ID, LOGIN, NAME, EMAIL, BIRTHDAY) VALUES
(1, "luke", "Luke", "luke@sw.com", to_date("1595.05.12", "YYYY.MM.DD")),
(2, "Obi-Wan", "Obi-Wan Kenobi", "obi-wan@kenobi.com", to_date("1484.12.2", "YYYY.MM.DD"));

ALTER SEQUENCE hibernate_sequence RESTART WITH 10;