# Project java-filmorate
## Model
### USER

### FILM

## Interface Storage
### Users

### Films

## Controllers
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
* * Если значение параметра count не задано, вернуть первые 10.