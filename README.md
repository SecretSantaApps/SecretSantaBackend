# Бекенд для игры «Тайный Санта»

__Стек__: Kotlin, Ktor, Ktorm, Docker, Docker Compose

---

### Подготовка к запуску

```shell
cp .env.sample .env

#отредактировать при необходимости
vim .env
```

---

## Получение ключа Firebase Admin SDK

[Документация](https://firebase.google.com/docs/admin/setup?authuser=0&hl=en)

Полученный файл необходимо будет поместить в корень проекта и дать соответствующее название как в docker-compose.yml

---

### Запуск сервера в Docker compose:

```shell
    docker-compose up --build -d
```

---

### Пересборка сервера при необходимости:

```shell
./gradlew shadowJar
```

---

### Остановка сервера:

```shell
docker-compose down
```

### Логи

```shell
docker-compose logs
```

---

# API v1

Для всех запросов в заголовок запроса необходимо поместить
```Authorization: Bearer $token$```
где $token$ – токен, полученный из клиента Firebase.

[Документация](https://firebase.google.com/docs/auth/admin/verify-id-tokens?hl=en&authuser=6&skip_cache=true#web)

Общие коды ответа:

```401 Unauthorized``` – Token expired or invalid

```200 OK``` - Operation performed successfully

```409 Conflict``` - Entity already exists

```400 Bad Request``` - Request body invalid or entity not found

```403 Forbidden``` - Current user not allowed to perform this operation

## 1) Пользователи

### Регистрация пользователя

```http request
POST /api/v1/user

body:
{
	"name": "Ivan" -- optional (если не указано, будет использовано имя из Firebase)
}

Response: 200 OK / 500 «Something went wrong» / 409 «User already exists»
```

### Удаление пользователя

```http request
DELETE /api/v1/user

Response: 200 OK / 500 «Something went wrong» / 400 «User not exists»
```

### Обновление пользователя

```http request
PATCH /api/v1/user

body:
{
	"name": "Ivan"
}

Response: 200 OK / 500 «Something went wrong» / 400 «User not exists»
```

### Аутентификация (верификация токена)

```http request
GET /api/v1/authenticate

Response: 200 OK / 401 Unauthorized
```

### Получение информации о пользователе

```http request
GET /api/v1/user

Response: 
{
	"user_id": "UwsdfgergdfDUFf2",
	"username": "Ivan",
	"recipient": "Kasldhg35tkRwsoyte" -- если игра начата
}

Response: 200 OK / 400 «User not exists»
```

### Получение информации о пользователе в определённой комнате по определённому user_id

```http request
GET /api/v1/user/info

Request: 
{
	"user_id": "UwsdfgergdfDUFf2",
	"room_name": "room1"
}

Response: 200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists»

body:

{
	"user_id": "UwsdfgergdfDUFf2",
	"username": "Ivan"
}
```

---

---

## 2) Комнаты

### Создание комнаты

```http request
POST /api/v1/room

body:
{
	"room_name":"room1",
	"password":"123456", --optional
	"max_price":1000, --optional 
	"date": "2020-01-31" --optional
}

Response: 200 OK / 500 «Something went wrong» / 400 «User not exists» / 409 «Room already exists»

body:
{
	"room_name": "room1",
	"password": "123456",
	"date": "2021-01-31",
	"owner_id": "UwsdfgergdfDUFf2",
	"max_price": 1000
}
```

### Удаление комнаты

```http request
DELETE /api/v1/room

body:
{
	"room_name":"room1"
}

Response: 200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists»
```

### Обновление данных комнаты

```http request
PATCH /api/v1/room

body:
{
	"room_name":"room1",
	"password": "2341234", --optional
	"date": "2020-01-31" --optional
}

Response: 200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden
```

### Получения информации о комнате (для администраторов)

```http request
GET /api/v1/room

body:
{
	"room_name":"room1"
}

Response: 200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden

body:
{
	"room_name": "room1",
	"password": "123456",
	"date": "2021-01-31",
	"owner_id": "UwsdfgergdfDUFf2",
	"max_price": 1000
}
```

---

---

## 3) Игра

### Подключение к игре (комнате)

```http request
POST /api/v1/game/join

body:
{
	"room_name":"room1",
	"password":"123456"
}

Response: 
200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden  
409 «Game already started» - к уже начавшейся игре невозможно присоединиться
409 «User already in room»

```

### Покидание комнаты

```http request
POST /api/v1/game/leave

body:
{
	"room_name":"room1"
}

Response: 
200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 «User not in the room»  
409 «Game already started» - из начавшейся игры нельзя выйти
```

**При покидании комнаты администратором, удаляется и комната**

### Изгнание пользователя из игры (комнаты). Только для администратора

```http request
POST /api/v1/game/kick

body:
{
	"user_id":"UwsdfgergdfDUFf2",
	"room_name":"room1"
}

Response: 
200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden  
409 «Game already started» - к уже начавшейся игре невозможно присоединиться
400 «User not in the room»
400 «You should use /leave instead of /kick» -если администратор попытался изгнать из комнаты самого себя
```

### Начало игры

**При вызове это команды (администратором), заблокируется вход и выход участников из комнаты, а также автоматически будут
распределены получатели подарков.**

```http request
POST /api/v1/game/start

body:
{
	"room_name":"room1"
}

Response: 
200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden  
409 «Game already started» 
400 «Not enough players to start playing» - для начала игры в комнате должно быть по крайней мере 3 человека
```

### Остановка игры

```http request
POST /api/v1/game/stop

body:
{
	"room_name":"room1"
}

Response: 
200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden  
409 «Game already stopped» 
```

### Получение информации о комнате (Для всех пользователей)

```http request
GET /api/v1/game/info

body:
{
	"room_name":"room1"
}

Response: 
200 OK / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden

body:
{
	"room_name": "room1",
	"users": [
		{
			"userId": "8Svq6y9zMhYQt6u",
			"username": "Ivan"
		},
		{
			"userId": "wv6H3etCbsV11dh6fgh",
			"username": "Petr"
		},
		{
			"userId": "UwTy8dFwunWFi6lYpx",
			"username": "Dmitry"
		}
	],
	"recipient": "UwTy8dFwunWFi6lYpx" -- если игра не начата, это поле будет отсутствовать
}
```