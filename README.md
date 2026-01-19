# Сlient Profile Service (Сервис Client Profile)

Сlient Profile Service — сервис по работе с персональными данными клиента банковской системы.  
Отвечает за управление клиентскими данными, а именно за получение, обновление и создание профиля клиента.
Также в сервисе реализовано кэширование данных через Redis.

---

## Основные возможности

- Создание профиля клиента посредством вычитки события из топика Kafka
- Получение профиля и кэширование полученных данных
- Обновление профиля и инвалидация кэша при обновлении
- JWT-валидация через JWKS endpoint Auth Service

---

## Используемые технологии

- Java 17
- Spring Boot 3
- Spring Security (OAuth2 Resource Server, JWT)
- Spring Data JPA
- PostgreSQL
- Liquibase
- Apache Kafka
- Redis
- MapStruct
- Docker / Docker Compose

---

## Используемые Kafka топики

- `auth.users` — события по созданию профиля клиента (USER_CREATED)
- `system.errors` — системные ошибки сервиса (SYSTEM_ERROR)

---

## Как запустить локально

### Запуск через Docker Compose

1. Поднять инфраструктуру и сервисы:

```bash
docker compose up -d
```

2. Проверить логи Сlient Profile Service:

```bash
docker logs -f client-profile-service
```

## API Endpoints

Все запросы требуют JWT access token.

Заголовок авторизации:

```http
Authorization: Bearer <access_token>
```

### Получить информацию о клиенте

**GET** `/api/v1/profiles`

### Обновить информацию о клиенте

**PUT** `/api/v1/profiles`

#### Пример запроса
```json
{
  "firstName": "First Name",
  "lastName": "Last Name",
  "middleName": "Middle Name",
  "phone": "+79629330400"
}
```

## Примечания

- Сервис проверяет JWT как **OAuth2 Resource Server**.
- Создание профиля клиента происходит через endpoint POST /auth/register сервиса **auth-service** и посредством последующей вычитки события из **Kafka**.
- Liquibase используется как **единственный источник истины** схемы базы данных.