# Project java-filmorate
## Branches Developer
### main
+ Ветка слияния с другими ветками разработки после успешного review
### controllers-films-users [9 sprint]
+ Настройка Maven
+ SpringBoot
+ Создание модели приложения
+ Примитивный функционал обслуживание данных в хранении 
### add-friends-likes [10 sprint]
+ Расширение модели: добавлены Friends для User и Likes для Film
+ Расширение функционала контроллеров.
## Хранение данных
Сейчас данные можно хранить в переменных Map<Long, T>,
где Long — ключ (идентификатор объекта), а T — сам объект (User или Film);
Доступ к объектам предоставляется по ключу.
Для добавления, изменения, в будущем и удаление, записей используется контроллер.

## Улучшение API приложения до соответствия REST.
Изменена архитектура приложения с помощью внедрения зависимостей.

Принцип работы приложения разделён на слои:
+ storage @Component — слой хранения данных (далее будет реализовано хранить данные в долговременном хранилище, чтобы они не зависели от перезапуска приложения.)
+ service @Service — сервисный слой. Обеспечивает безопасность сохранности данных в процессе работы приложения.
+ controller @RestController — обслуживание данных в хранении через сервисный слой.

## Model
### USER
* Идентификатор — id : Long. Условие: Positive
* Электронная почта — email : String. Условие: Pattern(Email)
* Логин — login : String. Условие: NoSpaces, Size[3..20]
* Имя — name : String.
* Дата рождения — birthday : LocalDate. Условие: Past
* Список друзей — friends : Set<Long>.
### FILM
* Идентификатор — id : Long. Условие: Positive
* Название — name : String. Условие: NotNull/NotBlank
* Описание — description : String. Условие: Size=200
* Дата релиза — releaseDate : LocalDate. Условие: After 28 DEC 1895
* Продолжительность фильма — duration : int. Условие: Positive
* Идентификаторы пользователей, поставивших like фильму — likes : Set<Long>.
## Storages
`@Component` — слой хранения данных (далее будет реализовано хранить данные в долговременном хранилище, чтобы они не зависели от перезапуска приложения.)
### Abstract MasterStorage.class : Generic <T t> для USER и FILM
    private Long generateId = 1L;

    protected Long increment() {
        return generateId++;
    }

    public abstract T create(T t);

    public abstract T update(T t);

    public abstract T get(Long id);

    public abstract List<T> getAll();

    public abstract boolean isExist(Long id);

### UserStorage
    private final Map<Long, User> storage;
+ реализация абстрактных методов

### FilmStorage
    private final Map<Long, Film> storage;
+ реализация абстрактных методов

## Services
`@Service` — сервисный слой. Обеспечивает безопасность сохранности данных в процессе работы приложения.
### Abstract MasterService
    private final MasterStorage<T> storage;

    public T create(T t) {
        return storage.create(t);
    }

    public T update(T t) {
        return storage.update(t);
    }

    public T get(Long id) {
        return storage.get(id);
    }

    public List<T> getAll() {
        return storage.getAll();
    }
### UserService
    @Override
    public User create(User user)
С проверкой имени и уникального логина

    @Override
    public User update(User user)
С проверкой имени и уникального логина

    public void addFriend(Long id, Long idFriend)
Добавить друга в список

    public void deleteFriend(Long supposedId, Long supposedIdFriend)
Удалить друга из списка

    public Set<User> getFriends(Long supposedId)
Получить список друзей

    public Set<User> getFriendsCommon(Long id, Long otherId)
Получить список общих друзей между пользователями

* private-методы:
* + валидация
* + списки друзей 

### FilmService
    private final MasterStorage<User> userStorage;

    public List<Film> getPopular(Long supposedCount)
Получить популярные фильмы у пользователей

    public void addLike(Long supposedId, Long supposedUserId)
Добавить лайк пользователя фильму

    public void deleteLike(Long supposedId, Long supposedUserId)
Удалить лайк пользователя фильму

## Controllers
`@RestController` — обслуживание данных в хранении через сервисный слой.
### Users
* POST /users — создание пользователя;
* PUT /users — обновление пользователя;
* PUT /users/{id}/friends/{friendId} — добавление в друзья.
* DELETE /users/{id}/friends/{friendId} — удаление из друзей.
* GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
* GET /users/{id}/friends/common/{otherId} — список общих друзей с другим пользователем.
* GET /users — получение списка всех пользователей.
### Films
* POST /films — Добавляет фильма.
* PUT /films — Обновляет фильма.
* PUT /films/{id}/like/{userId} — Пользователь ставит лайк фильму.
* DELETE /films/{id}/like/{userId} — Пользователь удаляет лайк.
* GET /films — Возвращает список всех фильмов
* GET /films/popular?count={count} — Возвращает список из первых count* фильмов по количеству лайков.
* + Если значение параметра count не задано, вернуть первые 10.