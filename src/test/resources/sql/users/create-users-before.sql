delete from USERS;

alter table USERS alter COLUMN ID RESTART with 1;

insert into USERS (ID, LOGIN, NAME, EMAIL, BIRTHDAY) values
(1, 'luke', 'Mark Hamill', 'luke@skywalker.com', '1951-9-25'),
(2, 'obi-wan', 'Alec Guinness', 'obi-wan@kenobi.com', '1914-4-2');
