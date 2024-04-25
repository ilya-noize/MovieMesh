# Project java-filmorate

Социальная сеть по оценке фильмов пользователями. В ней можно дружить

# Table of contents
- [API](#api)
    * [User](#user)
    * [Film](#film)
    * [Genre](#genre)
    * [MPA Rating](#mpa)
- [Branches developer](#branches-developer)
    * [controllers-films-users](#sp9)
    * [add-friends-likes](#sp10)
    * [add-database](#sp11)
- [Хранение данных](#storage)
- [Улучшение API приложения до соответствия REST.](#rest)
- [Models](#models)
    * [User](#m_user)
    * [Film](#m_film)
    * [Genre](#m_genre)
    * [MPA Rating](#m_mpa)
    * [FilmGenres](#m_film_genre)


## API
<a name="api"></a>

<a name="user">**User**</a>
* POST /users — создание пользователя;
* PUT /users — обновление пользователя;
* PUT /users/{id}/friends/{friendId} — добавление в друзья.
* DELETE /users/{id}/friends/{friendId} — удаление из друзей.
* GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
* GET /users/{id}/friends/common/{otherId} — список общих друзей с другим пользователем.
* GET /users — получение списка всех пользователей.

<a name="film">**Film**</a>
* POST /films — Добавление фильма.
* PUT /films — Обновляет фильма.
* PUT /films/{id}/like/{userId} — Пользователь ставит лайк фильму.
* DELETE /films/{id}/like/{userId} — Пользователь удаляет лайк.
* GET /films — Возвращает список всех фильмов
* GET /films/popular?count={count} — Возвращает список из первых count* фильмов по количеству лайков.
*
    + Если значение параметра count не задано, вернуть первые 10.

<a name="genre">**Genre**</a>
* GET /genres/{id} — получение существующего жанра.
* GET /genres — получение списка всех жанров.

<a name="mpa">**MPARating**</a>
* GET /mpa/{id} — получение существующего рейтинга.
* GET /mpa — получение списка всех рейтингов.

## Branches Developer
<a name="branches-developer"></a>
### [controllers-films-users](https://github.com/ilya-noize/java-filmorate/pull/4)
<a name="sp9"></a>

+ Настройка Maven.
+ SpringBoot.
+ Создание модели приложения.
+ Примитивный функционал обслуживание данных в памяти.

### [add-friends-likes](https://github.com/ilya-noize/java-filmorate/pull/6)
<a name="sp10"></a>

+ Расширение модели: добавлены Friends для User и Likes для Film.
+ Расширение функционала контроллеров.

### [add-database](https://github.com/ilya-noize/java-filmorate/pull/7)
<a name="sp11"></a>

<img src="src/main/resources/db_schema.svg" title="Схема БД" width="900" height="400"/>

+ Изменена реализация работы со всеми предыдущими моделями (а именно Film и User) на работу в базе данных (далее
  сокращённо **БД**).
+ Добавлена возможность оставлять симпатии пользователей к фильмам.
+ Изменён функционал работы Друзья. Теперь дружба пользователей не может быть взаимной по умолчанию. Больше похоже на
  подписку.
+ Расширение модели: Genres. Добавлены жанры к фильмам. Изменение и добавление новых жаров не предусмотрено техническим
  заданием.
+ Расширение модели: MPARating. Добавлены рейтинги к фильмам. Изменение и добавление новых рейтингов не предусмотрено
  техническим заданием.


## Хранение данных
<a name="storage"></a>

~~Сейчас данные можно хранить в переменных Map<Long , T>,
где Long — ключ (идентификатор объекта), а T — сам объект (User или Film);
Доступ к объектам предоставляется по ключу.~~
<p>
Сейчас данные можно хранить в БД.<br/>
Для добавления и редактирования (в будущем и удаления) записей используется контроллер.
</p>

## Улучшение API приложения до соответствия REST.
<a name="rest"></a>

Изменена архитектура приложения с помощью внедрения зависимостей.

Принцип работы приложения разделён на слои:
+ storage @Component — слой хранения данных (далее будет реализовано хранить данные в долговременном хранилище, чтобы они не зависели от перезапуска приложения.)
+ service @Service — сервисный слой. Обеспечивает безопасность сохранности данных в процессе работы приложения.
+ controller @RestController — обслуживание данных в хранении через сервисный слой.


## Models
<a name="models"></a>

### User
<a name="m_user"></a>

* Идентификатор — id : Long. Условие: Positive
* Электронная почта — email : String. Условие: Pattern(Email)
* Логин — login : String. Условие: NoSpaces, Size[3..20]
* Имя — name : String.
* Дата рождения — birthday : LocalDate. Условие: Past
* ~~Список друзей — friends : Set<Long>.~~ Update: Реализовано в БД. Это поле более не актуально.

### Film
<a name="m_films"></a>

* Идентификатор — id : Long. Условие: Positive
* Название — name : String. Условие: NotBlank
* Описание — description : String. Условие: Size=200
* Дата релиза — releaseDate : LocalDate. Условие: After 28 DEC 1895
* Продолжительность фильма — duration : int. Условие: Positive
* Update: Рейтинг Ассоциации кинематографистов – mpaRating : MPARating Условие: NotNull
* Update: Список жанров – genres : List<Genre>
* ~~Идентификаторы пользователей, поставивших like фильму — likes : Set<Long>.~~ Update: Реализовано в БД.
  Это поле более не актуально.

### Genre
<a name="m_genre"></a>

* Идентификатор — id : Long. Условие: Positive
* Название — name : String. Условие: NotBlank

### MPARating
<a name="m_mpa"></a>

* Идентификатор — id : Long. Условие: Positive
* Название — name : String. Условие: NotBlank
* Описание — description : String. Условие: NotBlank

### FilmGenres
<a name="m_film_genre"></a>

+ Идентификатор фильма — filmId : Long. Условие: Positive, NotNull
+ Жанр — genre : Genre. Условие: Positive, NotNull