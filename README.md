# Car Dealership Management System

Микросервисная backend-система для управления мультибрендовым автосалоном. Разработана в рамках университетского курса.

## Оглавление

- [Архитектура](#архитектура)
- [Технологический стек](#технологический-стек)
- [Функциональные возможности](#функциональные-возможности)
- [Структура проекта](#структура-проекта)
- [Запуск проекта](#запуск-проекта)
- [Документация API](#документация-api)

---

## Архитектура

Проект представляет собой **микросервисную архитектуру**, состоящую из двух независимых сервисов:

| Сервис | Ответственность |
|--------|-----------------|
| **OrderService** | Управление заказами, тест-драйвами, клиентскими сценариями |
| **StorageService** | Управление складскими остатками, каталогом автомобилей и запчастей |

### Взаимодействие между сервисами

- **Асинхронное** — через брокер сообщений (Kafka).
- **Синхронное** — через **gRPC** для получения актуального списка автомобилей в наличии.

### Ключевые архитектурные решения

- **Event-Driven Architecture** — слабая связанность сервисов.
- **Hexagonal/Onion Architecture** — изоляция бизнес-логики от внешних слоёв.
- **Outbox Pattern** — атомарность изменения статуса заказа и публикации события.
- **Distributed Tracing** — сквозная наблюдаемость через OpenTelemetry.

---

## Технологический стек

### Языки и фреймворки
- **Java** — основной язык
- **Spring Boot 3** — основа приложения
- **Spring Data JPA** — работа с БД
- **Spring Security** — аутентификация и авторизация
- **Hibernate** — ORM

### Базы данных
- **PostgreSQL** — основная реляционная БД
- **Liquibase** — управление миграциями

### Межсервисное взаимодействие
- **Kafka** — брокер сообщений
- **gRPC** — синхронный RPC

### Безопасность
- **Keycloak** — централизованная аутентификация и управление ролями
- **JWT** — токены доступа
- **Spring Security** — авторизация на уровне методов (`@PreAuthorize`)

### Мониторинг
- **OpenTelemetry** — сбор трейсов и метрик
- **Prometheus** — хранение метрик
- **Grafana** — визуализация дашбордов

---

## Функциональные возможности

### OrderService
- Создание, просмотр и отмена заказов
- Управление заявками на тест-драйв
- Ролевая модель доступа (USER, MANAGER, ADMIN)
- Проверка владельца сущности через `@PreAuthorize`
- Публикация доменных событий

### StorageService
- Управление каталогом автомобилей и запчастей
- Ведение складских остатков
- Проверка доступности комплектующих
- Обработка складских операций
- Создание и сопровождение внутреннего **заказа на сборку**

### Конфигуратор автомобилей
- Выбор комплектации из доступных узлов
- Проверка совместимости компонентов
- Расчёт итоговой стоимости
- Предметные исключения (`IncompatibleComponentException`, `DomainValidationException`)

---

## Структура проекта
```
├── common/ # Общие компоненты для всех сервисов
│ └── src/main/java/study/project/dealership/common/
│ ├── messaging/ # Общие классы для работы с брокерами
│ └── observability/ # OpenTelemetry, логирование, трейсинг
│
├── docker/ # Docker-конфигурации
│ ├── init-db/ # Скрипты инициализации БД
│ ├── keycloak/ # Конфигурация Keycloak (realm, клиенты)
│ │ ├── import/ # Экспортированный realm для импорта
│ │ └── postgres/ # Данные PostgreSQL для Keycloak
│ └── observability/ # Prometheus, Grafana, дашборды
│ └── grafana/provisioning/ # Автоматическая настройка дашбордов
│
├── gradle/ # Gradle Wrapper
│ └── wrapper/
│
├── order-service/ # Сервис заказов
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/study/project/dealership/orders/
│ │ │ │ ├── OrderServiceApplication.java
│ │ │ │ ├── abstractions/ # Абстракции (интерфейсы репозиториев)
│ │ │ │ ├── application/ # Бизнес-логика (Service Layer)
│ │ │ │ │ ├── exception/ # Предметные исключения
│ │ │ │ │ └── services/ # Реализация Use Cases
│ │ │ │ ├── configuration/ # Spring-конфигурации
│ │ │ │ ├── contracts/ # DTO для API
│ │ │ │ │ ├── car/ # DTO для автомобилей
│ │ │ │ │ ├── mapping/ # DTO для мапперов
│ │ │ │ │ ├── order/ # DTO для заказов
│ │ │ │ │ ├── part/ # DTO для запчастей
│ │ │ │ │ ├── request/ # Request DTO
│ │ │ │ │ └── user/ # DTO для пользователей
│ │ │ │ ├── domain/ # Доменная модель (Entity + VO)
│ │ │ │ │ ├── order/ # Order aggregate
│ │ │ │ │ ├── request/ # Request aggregate
│ │ │ │ │ ├── user/ # User aggregate
│ │ │ │ │ └── valueobject/ # Value Objects
│ │ │ │ ├── infrastructure/ # Инфраструктурный слой
│ │ │ │ │ ├── client/ # gRPC клиент для StorageService
│ │ │ │ │ ├── database/ # JPA-репозитории
│ │ │ │ │ │ └── repository/ # Реализация репозиториев
│ │ │ │ │ ├── grpc/ # gRPC-клиенты
│ │ │ │ │ ├── messaging/ # Kafka (Outbox + Inbox)
│ │ │ │ │ │ ├── inbox/ # Inbox-обработчик
│ │ │ │ │ │ └── outbox/ # Outbox-паттерн
│ │ │ │ │ └── repository/ # Доп. репозитории
│ │ │ │ └── presentation/ # Слой представления (REST API)
│ │ │ │ ├── api/ # API-контракты (Swagger)
│ │ │ │ └── controllers/ # REST-контроллеры
│ │ │ └── resources/
│ │ │ ├── application.yml
│ │ │ ├── db/changelog/ # Liquibase миграции
│ │ │ └── logback-spring.xml
│ │ ├── integrationTest/ # Интеграционные тесты
│ │ └── test/ # Модульные тесты
│ ├── build.gradle
│ └── settings.gradle
│
├── storage-service/ # Складской сервис
│ └── ... (аналогичная структура order-service)
│
├── proto/ # .proto-контракты для gRPC
│ └── storage.proto
│
├── docker-compose.yml # Запуск всех сервисов
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── .env.example
```

## Запуск проекта

### Требования
- Java 21
- Docker и Docker Compose

### Шаги для запуска

1. **Клонируйте репозиторий**
```bash
git clone https://github.com/your-username/car-dealership.git
cd car-dealership
```

2. **Запустите все сервисы через Docker Compose**

```bash
docker-compose up -d
```

4. Проверьте работу сервисов

- OrderService: `http://localhost:8081/swagger-ui.html`
- StorageService: `http://localhost:8082/swagger-ui.html`
- Keycloak: `http://localhost:8080`
- Prometheus: `http://localhost:9091`
- Grafana: `http://localhost:3000`

### Документация API
Swagger UI доступен после запуска сервисов:

- OrderService: `/swagger-ui.html`

- StorageService: `/swagger-ui.html`

Документация генерируется автоматически через SpringDoc OpenAPI.

