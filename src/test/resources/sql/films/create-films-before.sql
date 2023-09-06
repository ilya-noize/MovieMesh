DELETE FROM FILMS;

INSERT INTO FILMS (ID, NAME, DESCRIPTION, DURATION, RELEASEDATE, MPA_RATING_ID) VALUES
-- Test Film:film1
(1, 'StarWars:Episode I', 'A long time ago in a galaxy far, far away', 120, '1999-05-19', 2),
-- Test Film:film2
(2, 'StarWars:Episode II', 'A long time ago in a galaxy far, far away', 120, '2002-05-16', 2),
-- Test Film:emptyName
(3, 'StarWars:Episode III', 'A long time ago in a galaxy far, far away', 120, '2005-05-19', 2),
(4, 'StarWars:Episode IV', 'A long time ago in a galaxy far, far away', 120, '1977-05-25', 2),
(5, 'StarWars:Episode V', 'A long time ago in a galaxy far, far away', 120, '1980-05-21', 2),
(6, 'StarWars:Episode VI', 'A long time ago in a galaxy far, far away', 120, '1883-05-25', 2),
(7, 'StarWars:Episode VII', 'A long time ago in a galaxy far, far away', 120, '3015-12-18', 2),
(8, 'StarWars:Episode VIII', 'A long time ago in a galaxy far, far away', -120, '2017-12-15', 2),
(9, 'StarWars:Episode IX', 'A long time ago in a galaxy far, far away', 120, '2019-12-19', 2);

--ALTER SEQUENCE hibernate_sequence RESTART WITH 10;

ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1;