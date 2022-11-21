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

---

# API

Для всех запросов в заголовок запроса необходимо поместить
```Authorization: Bearer $token$```
где $token$ – токен, полученный из клиента Firebase.

[Документация](https://firebase.google.com/docs/auth/admin/verify-id-tokens?hl=en&authuser=6&skip_cache=true#web)

# 1) Пользователи

### Регистрация пользователя

```http request
POST /api/v1/user

body:
{
	"name": "Ivan" // опционально, иначе будет 
	//использовано имя из токена Firebase
}

Response: 200 OK / 409 Conflict
```

### Удаление пользователя

```http request
DELETE /api/v1/user

Response: 200 OK
```

### Обновление пользователя

```http request
PATCH /api/v1/user

body:
{
	"name": "Ivan"
}

Response: 200 OK / 409 Conflict
```

### Аутентификация (верификация токена)

```http request
GET /api/v1/authenticate

Response: 200 OK / 401 Unauthorized

401 – token expired or invalid 
```

### Получение информации о пользователе

```http request
GET /api/v1/user

Response: 
{
	"userId": "UwsdfgergdfDUFf2",
	"username": "Ivan"
}
```

---

---

## TO BE CONTINUED
