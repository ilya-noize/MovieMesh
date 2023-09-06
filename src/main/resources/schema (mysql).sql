-- Exported from QuickDBD: https://www.quickdatabasediagrams.com/
-- NOTE! If you have used non-SQL datatypes in your design, you will have to change these here.

-- Modify this code to update the DB schema diagram.
-- To reset the sample schema, replace everything with
-- two dots ('..' - without quotes).

create TABLE `Films` (
    `ID` int  NOT NULL ,
    `Title` varchar(200)  NOT NULL ,
    `decription` varchar(200)  NOT NULL ,
    `duration` int  NOT NULL ,
    `release` date  NOT NULL ,
    `mpa_rating_id` int  NOT NULL ,
    PRIMARY KEY (
        `ID`
    )
);

create TABLE `Users` (
    `ID` int  NOT NULL ,
    `login` string  NOT NULL ,
    `name` string  NOT NULL ,
    `email` string  NOT NULL ,
    `birthday` date  NOT NULL ,
    PRIMARY KEY (
        `ID`
    ),
    CONSTRAINT `uc_Users_login` UNIQUE (
        `login`
    )
);

create TABLE `MPA_Rating` (
    `ID` int  NOT NULL ,
    `rating` varchar(10)  NOT NULL ,
    `decription` varchar(100)  NOT NULL ,
    PRIMARY KEY (
        `ID`
    )
);

create TABLE `Genres` (
    `ID` int  NOT NULL ,
    `genre` varchar(32)  NOT NULL ,
    PRIMARY KEY (
        `ID`
    )
);

create TABLE `Genres_film` (
    `film_id` int  NOT NULL ,
    `genre_id` int  NOT NULL 
);

create TABLE `Friends_users` (
    `user_id_request` int  NOT NULL ,
    `user_id_friend` int  NOT NULL
);

create TABLE `Films_likes` (
    `film_id` int  NOT NULL ,
    `user_id` int  NOT NULL 
);

alter table `Films` ADD CONSTRAINT `fk_Films_mpa_rating_id` FOREIGN KEY(`mpa_rating_id`)
REFERENCES `MPA_Rating` (`ID`);

alter table `Genres_film` ADD CONSTRAINT `fk_Genres_film_film_id` FOREIGN KEY(`film_id`)
REFERENCES `Films` (`ID`);

alter table `Genres_film` ADD CONSTRAINT `fk_Genres_film_genre_id` FOREIGN KEY(`genre_id`)
REFERENCES `Genres` (`ID`);

alter table `Friends_users` ADD CONSTRAINT `fk_Friends_users_user_id_request` FOREIGN KEY(`user_id_request`)
REFERENCES `Users` (`ID`);

alter table `Friends_users` ADD CONSTRAINT `fk_Friends_users_user_id_friend` FOREIGN KEY(`user_id_friend`)
REFERENCES `Users` (`ID`);

alter table `Films_likes` ADD CONSTRAINT `fk_Films_likes_film_id` FOREIGN KEY(`film_id`)
REFERENCES `Films` (`ID`);

alter table `Films_likes` ADD CONSTRAINT `fk_Films_likes_user_id` FOREIGN KEY(`user_id`)
REFERENCES `Users` (`ID`);

create index `idx_Films_Title`
ON `Films` (`Title`);

create index `idx_MPA_Rating_rating`
ON `MPA_Rating` (`rating`);

create index `idx_Genres_genre`
ON `Genres` (`genre`);

create index `idx_Genres_film_film_id`
ON `Genres_film` (`film_id`);

create index `idx_Genres_film_genre_id`
ON `Genres_film` (`genre_id`);

create index `idx_Friends_users_user_id_request`
ON `Friends_users` (`user_id_request`);

create index `idx_Friends_users_user_id_friend`
ON `Friends_users` (`user_id_friend`);

