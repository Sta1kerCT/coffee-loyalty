# Документация по интерфейсам и алгоритмам

## 220 — Архитектура

**Компоненты:**
- **coffee-loyalty** — монолитное Spring Boot приложение
- **БД:** H2 (dev) / PostgreSQL (docker, k8s). Таблицы: client, reward, purchase, point_transaction.
- **Kafka:** топик `point-transactions` дельта баллов. DLQ: `point-transactions-dlq`.
- **Мониторинг:** Actuator, Prometheus scrape, Grafana дашборды и алерты.

**Потоки данных:**
- Регистрация покупки → начисление баллов → запись в БД → отправка положительного числа в Kafka.
- Обмен на награду → списание баллов → запись в БД → отправка отрицательного числа в Kafka.
- ClickHouse читает топик Kafka, materialized view считает сумму положительных и отрицательных.

---

## 230 — Сервисы (API)

Формат тел запросов: JSON. Валидация: @Valid на DTO (NotBlank, NotNull, Positive где нужно).

**Клиенты**
- GET `/api/clients` — список клиентов. query-параметр `search` (поиск по ФИО/телефону).
- GET `/api/clients/{id}` — клиент по ID.
- POST `/api/clients` — создание клиента (тело: fullName, phone, email).
- PUT `/api/clients/{id}` — обновление клиента.
- DELETE `/api/clients/{id}` — удаление клиента.

**Награды**
- GET `/api/rewards` — список наград.
- GET `/api/rewards/{id}` — награда по ID.
- POST `/api/rewards` — создание награды (тело: name, pointsCost).
- PUT `/api/rewards/{id}` — обновление награды.
- DELETE `/api/rewards/{id}` — удаление награды.

**Покупки**
- GET `/api/purchases` — список всех покупок.
- GET `/api/purchases/client/{clientId}` — покупки по клиенту.
- GET `/api/purchases/{id}` — покупка по ID.
- POST `/api/purchases` — регистрация покупки (тело: clientId, amountRub; pointsEarned опционально, иначе считается по правилу 1 балл = 10 руб.).

**Транзакции баллов**
- GET `/api/transactions/client/{clientId}` — транзакции баллов по клиенту.
- GET `/api/transactions/{id}` — транзакция по ID.

**Обмен баллов**
- POST `/api/redeem` — обмен баллов на награду (тело: clientId, rewardId).

---

## 240 — UI

- **Прототип:** draw.io — `ui-wireframes.drawio`.
- **Реализация:** статическая страница `src/main/resources/static/index.html` — список клиентов и наград, вызовы REST API.
- **Экраны:** Список клиентов, форма клиента, список наград, форма награды, регистрация покупки, обмен на награду, транзакции по клиенту.
