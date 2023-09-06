DELETE FROM FILMS;

INSERT INTO FILMS (ID, NAME, DESCRIPTION, DURATION, RELEASEDATE, MPA_RATING_ID) VALUES
-- Test Film:film1
(1, "StarWars:Episode I", "A long time ago in a galaxy far, far away", 120, to_date("1999-05-19", "YYYY.MM.DD"), 2),
-- Test Film:film2
(2, "StarWars:Episode II", "A long time ago in a galaxy far, far away", 120, to_date("2002-05-16", "YYYY.MM.DD"), 2),
-- Test Film:emptyName
(3, "StarWars:Episode III", "A long time ago in a galaxy far, far away", 120, to_date("2005-05-19", "YYYY.MM.DD"), 2),
-- Test Film:nullName
(4, "StarWars:Episode IV", "A long time ago in a galaxy far, far away", 120, to_date("1977-05-25", "YYYY.MM.DD"), 2),
-- Test Film:moreLimitDescription
(5, "StarWars:Episode V", "A long time ago in a galaxy far, far away", 120, to_date("1980-05-21", "YYYY.MM.DD"), 2),
-- Test Film:wrongReleaseOverLimit
(6, "StarWars:Episode VI", "A long time ago in a galaxy far, far away", 120, to_date("1883-05-25", "YYYY.MM.DD"), 2),
-- Test Film:wrongReleaseInFuture
(7, "StarWars:Episode VII", "A long time ago in a galaxy far, far away", 120, to_date("3015-12-18", "YYYY.MM.DD"), 2),
-- Test Film:negativeDuration
(8, "StarWars:Episode VIII", "A long time ago in a galaxy far, far away", -120, to_date("2017-12-15", "YYYY.MM.DD"), 2),
-- Test Film:wrongId
(9, "StarWars:Episode IX", "A long time ago in a galaxy far, far away", 120, to_date("2019-12-19", "YYYY.MM.DD"), 2);

ALTER SEQUENCE hibernate_sequence RESTART WITH 10;