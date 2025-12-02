# Employee Service

REST API для управления сотрудниками, отделами, обучениями и пропусками. Приложение построено на Spring Boot 4.0 с использованием JWT аутентификации, Flyway миграций и Swagger/OpenAPI документации.

## ⚡ Быстрый старт

```bash
# 1. Запустите MySQL (или используйте Docker Compose)
docker-compose up -d mysql

# 2. Запустите приложение
mvn spring-boot:run

# 3. Откройте Swagger UI
# http://localhost:8080/swagger-ui.html

# 4. Получите токен через /auth/login и авторизуйтесь в Swagger
```

## 🚀 Возможности

- ✅ Управление сотрудниками (CRUD операции)
- ✅ Управление отделами
- ✅ Управление обучениями сотрудников
- ✅ Управление пропусками (отпуска, больничные)
- ✅ JWT аутентификация и авторизация
- ✅ Swagger/OpenAPI документация
- ✅ Автоматические миграции базы данных (Flyway)
- ✅ RESTful API с обработкой ошибок

## 📋 Требования

- **Java 21** или выше
- **Maven 3.6+**
- **MySQL 8.0+** (или Docker для запуска через docker-compose)
- **Docker** и **Docker Compose** (опционально, для контейнеризации)

## 🛠️ Установка и запуск

### Вариант 1: Локальный запуск

#### 1. Настройка базы данных MySQL

Создайте базу данных MySQL:

```sql
CREATE DATABASE employee_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'mysql'@'localhost' IDENTIFIED BY 'mysql';
GRANT ALL PRIVILEGES ON employee_db.* TO 'mysql'@'localhost';
FLUSH PRIVILEGES;
```

Или используйте существующую базу данных и обновите настройки в `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
```

#### 2. Настройка переменных окружения (опционально)

Для production окружения рекомендуется использовать переменные окружения:

```bash
# Windows (PowerShell)
$env:APP_JWT_SECRET="your-very-strong-secret-key-minimum-256-bits"
$env:APP_JWT_EXPIRATION_MS="3600000"

# Linux/Mac
export APP_JWT_SECRET="your-very-strong-secret-key-minimum-256-bits"
export APP_JWT_EXPIRATION_MS="3600000"
```

#### 3. Сборка и запуск приложения

```bash
# Сборка проекта
mvn clean install

# Запуск приложения
mvn spring-boot:run
```

Или используйте встроенный Maven wrapper:

```bash
# Windows
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw clean install
./mvnw spring-boot:run
```

Приложение запустится на порту **8080**.

### Вариант 2: Запуск через Docker Compose

#### 1. Запуск всех сервисов

```bash
docker-compose up -d
```

Эта команда запустит:
- MySQL базу данных на порту 3306
- Spring Boot приложение на порту 8080

#### 2. Просмотр логов

```bash
# Все сервисы
docker-compose logs -f

# Только backend
docker-compose logs -f backend

# Только MySQL
docker-compose logs -f mysql
```

#### 3. Остановка сервисов

```bash
docker-compose down
```

Для удаления данных базы данных:

```bash
docker-compose down -v
```

## 📚 Использование Swagger UI

### Доступ к Swagger UI

После запуска приложения откройте в браузере:

```
http://localhost:8080/swagger-ui.html
```

или

```
http://localhost:8080/swagger-ui/index.html
```

### JSON документация API

OpenAPI спецификация доступна по адресу:

```
http://localhost:8080/v3/api-docs
```

### Работа с Swagger UI

#### 1. Просмотр API документации

- В Swagger UI вы увидите все доступные endpoints, сгруппированные по категориям:
  - **Authentication** - аутентификация
  - **Employees** - управление сотрудниками
  - **Departments** - управление отделами
  - **Trainings** - управления обучениями
  - **Absences** - управление пропусками

#### 2. Тестирование API без авторизации

Некоторые endpoints доступны без авторизации:
- `POST /v1/employee-service/auth/login` - вход в систему
- `POST /v1/employee-service/auth/register` - регистрация (требует авторизации ADMIN)

#### 3. Авторизация через JWT токен

Для доступа к защищенным endpoints:

1. **Получите JWT токен:**
   - Откройте endpoint `POST /v1/employee-service/auth/login`
   - Нажмите "Try it out"
   - Введите данные пользователя:
     ```json
     {
       "username": "admin",
       "password": "your_password"
     }
     ```
   - Нажмите "Execute"
   - Скопируйте токен из ответа (поле `token`)

2. **Авторизуйтесь в Swagger:**
   - Нажмите кнопку **"Authorize"** (🔒) в правом верхнем углу
   - В поле "Value" введите токен в формате:
     ```
     Bearer ваш_токен_здесь
     ```
     Например:
     ```
     Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     ```
   - Нажмите "Authorize"
   - Закройте окно

3. **Теперь все защищенные endpoints доступны:**
   - Вы можете тестировать любые CRUD операции
   - Токен будет автоматически добавляться к каждому запросу

#### 4. Примеры использования Swagger

**Создание сотрудника:**
1. Откройте `POST /v1/employee-service/employees`
2. Нажмите "Try it out"
3. Заполните JSON:
   ```json
   {
     "fullName": "Иванов Иван Иванович",
     "gender": "М",
     "hireDate": "2024-01-15",
     "competenceRank": "MIDDLE",
     "competenceLevel": 2,
     "departmentId": 1
   }
   ```
4. Нажмите "Execute"
5. Проверьте ответ (код 201 Created)

**Получение списка сотрудников:**
1. Откройте `GET /v1/employee-service/employees`
2. Нажмите "Try it out"
3. Нажмите "Execute"
4. Проверьте список сотрудников в ответе

## 🔐 Аутентификация

### Получение JWT токена

```bash
curl -X POST http://localhost:8080/v1/employee-service/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "your_password"
  }'
```

Ответ:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Использование токена в запросах

```bash
curl -X GET http://localhost:8080/v1/employee-service/employees \
  -H "Authorization: Bearer ваш_токен_здесь"
```

## 📡 API Endpoints

### Authentication
- `POST /v1/employee-service/auth/login` - Вход в систему
- `POST /v1/employee-service/auth/register` - Регистрация пользователя (требует роль ADMIN)

### Employees
- `GET /v1/employee-service/employees` - Получить всех сотрудников
- `GET /v1/employee-service/employees/{id}` - Получить сотрудника по ID
- `POST /v1/employee-service/employees` - Создать сотрудника
- `PUT /v1/employee-service/employees/{id}` - Обновить сотрудника
- `DELETE /v1/employee-service/employees/{id}` - Удалить сотрудника

### Departments
- `GET /v1/employee-service/departments` - Получить все отделы
- `GET /v1/employee-service/departments/{id}` - Получить отдел по ID
- `POST /v1/employee-service/departments` - Создать отдел
- `PUT /v1/employee-service/departments/{id}` - Обновить отдел
- `DELETE /v1/employee-service/departments/{id}` - Удалить отдел

### Trainings
- `GET /v1/employee-service/trainings` - Получить все обучения
- `GET /v1/employee-service/trainings/{id}` - Получить обучение по ID
- `GET /v1/employee-service/trainings/employee/{employeeId}` - Получить обучения сотрудника
- `POST /v1/employee-service/trainings` - Создать обучение
- `PUT /v1/employee-service/trainings/{id}` - Обновить обучение
- `DELETE /v1/employee-service/trainings/{id}` - Удалить обучение

### Absences
- `GET /v1/employee-service/absences` - Получить все пропуски
- `GET /v1/employee-service/absences/{id}` - Получить пропуск по ID
- `GET /v1/employee-service/absences/employee/{employeeId}` - Получить пропуски сотрудника
- `POST /v1/employee-service/absences` - Создать пропуск
- `PUT /v1/employee-service/absences/{id}` - Обновить пропуск
- `DELETE /v1/employee-service/absences/{id}` - Удалить пропуск

## ⚙️ Конфигурация

### Основные настройки (application.yaml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/employee_db
    username: mysql
    password: mysql
  flyway:
    enabled: true
    baseline-on-migrate: true

app:
  jwt:
    secret: ${APP_JWT_SECRET:default-secret}
    expiration-ms: ${APP_JWT_EXPIRATION_MS:3600000}
```

### Переменные окружения

| Переменная | Описание | По умолчанию |
|-----------|----------|--------------|
| `APP_JWT_SECRET` | Секретный ключ для JWT | `SuperStrongSecretKeyForJWT_ChangeMe_2025_Minimum256BitsRequired` |
| `APP_JWT_EXPIRATION_MS` | Время жизни токена (мс) | `3600000` (1 час) |
| `SPRING_DATASOURCE_URL` | URL базы данных | `jdbc:mysql://localhost:3306/employee_db` |
| `SPRING_DATASOURCE_USERNAME` | Имя пользователя БД | `mysql` |
| `SPRING_DATASOURCE_PASSWORD` | Пароль БД | `mysql` |

## 🗄️ База данных

### Миграции Flyway

Миграции автоматически применяются при запуске приложения из директории:
```
src/main/resources/db/migration/
```

Файлы миграций:
- `V1__init_tables.sql` - Создание таблиц
- `V2__initial_data.sql` - Начальные данные

### Структура базы данных

- **users** - Пользователи системы
- **departments** - Отделы
- **employees** - Сотрудники
- **trainings** - Обучения
- **absences** - Пропуски

## 🧪 Тестирование

### Запуск тестов

```bash
mvn test
```

### Проверка работоспособности

После запуска приложения проверьте:

1. **Health check:**
   ```bash
   curl http://localhost:8080/v3/api-docs
   ```

2. **Swagger UI:**
   Откройте http://localhost:8080/swagger-ui.html

3. **Авторизация:**
   ```bash
   curl -X POST http://localhost:8080/v1/employee-service/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"your_password"}'
   ```

## 🐛 Решение проблем

### Проблема: База данных не подключается

**Решение:**
- Убедитесь, что MySQL запущен
- Проверьте настройки в `application.yaml`
- Проверьте, что база данных `employee_db` создана

### Проблема: Swagger UI не открывается

**Решение:**
- Убедитесь, что приложение запущено на порту 8080
- Проверьте URL: `http://localhost:8080/swagger-ui.html`
- Проверьте логи приложения на наличие ошибок

### Проблема: 401 Unauthorized

**Решение:**
- Убедитесь, что вы авторизованы через кнопку "Authorize" в Swagger
- Проверьте, что токен не истек (по умолчанию 1 час)
- Получите новый токен через `/auth/login`

### Проблема: Миграции не применяются

**Решение:**
- Проверьте логи приложения на наличие ошибок Flyway
- Убедитесь, что файлы миграций находятся в `src/main/resources/db/migration/`
- Проверьте, что `spring.flyway.enabled=true` в конфигурации

## 📝 Примеры использования

### Создание сотрудника через cURL

```bash
curl -X POST http://localhost:8080/v1/employee-service/employees \
  -H "Authorization: Bearer ваш_токен" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Петров Петр Петрович",
    "gender": "М",
    "hireDate": "2024-02-01",
    "competenceRank": "SENIOR",
    "competenceLevel": 3,
    "departmentId": 1
  }'
```

### Получение всех отделов

```bash
curl -X GET http://localhost:8080/v1/employee-service/departments \
  -H "Authorization: Bearer ваш_токен"
```

## 📄 Лицензия

Этот проект использует Apache 2.0 лицензию.

## 👥 Авторы

Employee Service Team

---

**Примечание:** Для production окружения обязательно измените секретный ключ JWT и используйте переменные окружения для хранения секретов!

