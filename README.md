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

```400 Wrong room id``` - Wrong room id in query parameters

## 1) Пользователи

### Регистрация пользователя

```http request
POST /api/v1/user

body:
{
	"username": "Ivan" -- optional (если не указано, будет использовано имя из Firebase)
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
	"username": "Ivan"
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

Response: 200 OK / 400 «User not exists»
body: 
{
	"user_id": "UwsdfgergdfDUFf2",
	"username": "Ivan"
}
```

### Получение информации о комнатах, в которых состоит пользователь

```http request
GET /api/v1/user/rooms

Response: 200 OK / 400 «User not exists»
body: 
[
	{
		"room_name": "room1",
		"room_id": "080070e"
		"date": "2021-01-31",
		"owner_id": "8Svq6y9zMhYQt6u48",
		"members_count": 2
	},
	{
		"room_name": "room2",
		"room_id": "sd3fsq5"
		"date": "2021-01-31",
		"owner_id": "pHEIpLQPv8YOqzn06",
		"members_count": 3
	}
]
```

### Получение информации о другом пользователе

```http request
GET /api/v1/user?id=UwsdfgergdfDUFf2

Response: 200 OK / 500 «Something went wrong» / 400 «User not exists» 

body:

{
	"user_id": "UwsdfgergdfDUFf2",
	"username": "Ivan",
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

Response: 200 OK / 500 «Something went wrong» / 400 «User not exists» 

body:
{
	"room_name": "room1",
	"password": "123456",
	"room_id": "080070e",
	"date": "2021-01-31",
	"owner_id": "UwsdfgergdfDUFf2",
	"max_price": 1000
}
```

### Удаление комнаты

```http request
DELETE /api/v1/room?id=080070e

Response: 200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 400 «Wrong room id»
```

### Обновление данных комнаты

```http request
PATCH /api/v1/room?id=080070e

body:
{
	"room_name":"room1", --optional
	"password": "2341234", --optional
	"date": "2020-01-31", --optional
	"max_price": "2000" --optional
}

Response: 200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden / 400 «Wrong room id»
```

### Получения информации о комнате (для администраторов)

```http request
GET /api/v1/room?id=080070e


Response: 200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden / 400 «Wrong room id»

body:
{
	"room_name": "room1",
	"password": "123456",
	"room_id": "080070e",
	"date": "2021-01-31",
	"owner_id": "UwsdfgergdfDUFf2",
	"max_price": 1000,
	"members_count": 1
}
```

---

---

## 3) Игра

### Подключение к игре (комнате)

```http request
POST /api/v1/game/join?id=080070e&pass=123456

Response: 
200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden  
409 «Game already started» - к уже начавшейся игре невозможно присоединиться
409 «User already in room»
400 «Wrong room id»

```

### Покидание комнаты

```http request
POST /api/v1/game/leave?id=080070e

Response: 
200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 «User not in the room»  
409 «Game already started» - из начавшейся игры нельзя выйти
400 «Wrong room id»
```

**При покидании комнаты администратором, удаляется и комната**

### Изгнание пользователя из игры (комнаты). Только для администратора

```http request
POST /api/v1/game/kick

body:
{
	"user_id":"UwsdfgergdfDUFf2",
	"room_id":"080070e"
}

Response: 
200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden  
409 «Game already started» - к уже начавшейся игре невозможно присоединиться
400 «User not in the room»
400 «You should use /leave instead of /kick» -если администратор попытался изгнать из комнаты самого себя
```

### Начало игры

**При вызове это команды (администратором), заблокируется вход и выход участников из комнаты, а также автоматически
будут
распределены получатели подарков.**

```http request
POST /api/v1/game/start?id=080070e

Response: 
200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden  
409 «Game already started» 
400 «Not enough players to start playing» - для начала игры в комнате должно быть по крайней мере 3 человека
```

### Остановка игры

```http request
POST /api/v1/game/stop?id=080070e

Response: 
200 OK / 500 «Something went wrong» / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden  
409 «Game already stopped» 
```

### Получение информации о игре (Для всех пользователей)

```http request
GET /api/v1/game/info?id=080070e

Response: 
200 OK / 400 «User not exists» / 400 «Room not exists» / 403 Forbidden / 400 Wrong room name

body:
{
  "room_id": "080070e",
	"room_name": "room1",
	"owner_id": "8Svq6y9zMhYQt6u",
	"date": "2023-01-31",
	"password": "d5f7496",
	"max_price": 1000,
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