# Как запустить проект

---

## 1. Сборка

```bash
./mvnw clean package -DskipTests
```

в `target/` появится `coffee-loyalty-1.0.0-SNAPSHOT.jar`.

---

## 2. Запуск приложения (локально, без Kafka)

```bash
KAFKA_ENABLED=false ./mvnw spring-boot:run
```

После старта:

- **Главная:** http://localhost:8080/
- **Health:** http://localhost:8080/actuator/health
- **Метрики Prometheus:** http://localhost:8080/actuator/prometheus

БД по умолчанию H2 в памяти.

## 3. Проверка API (Postman / curl / PowerShell)

**Импорт коллекции:** в Postman — Import → выбрать файл `postman/Coffee-Loyalty-API.postman_collection.json`. Переменная `baseUrl` по умолчанию `http://localhost:8080`.


```bash
# Создать клиента
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Тест","phone":"+7900","email":"t@t.ru"}'

# Список клиентов
curl http://localhost:8080/api/clients

# Создать награду
curl -X POST http://localhost:8080/api/rewards \
  -H "Content-Type: application/json" \
  -d '{"name":"Кофе в подарок","pointsCost":50}'

# Регистрация покупки (подставь свой clientId)
curl -X POST http://localhost:8080/api/purchases \
  -H "Content-Type: application/json" \
  -d '{"clientId":1,"amountRub":300}'
```

В Postman можно по очереди выполнить запросы из папок «Clients», «Rewards», «Purchases», «Redeem», «Transactions».

---

## 4. Автотесты

```bash
./mvnw test
```
```bash
./mvnw test -P ui-tests
```
```bash
./mvnw test -Dtest=ClientApiTest
```

---

## 5. Запуск всего стека в Docker (app + БД + Kafka + Prometheus + Grafana)

Сначала собрать JAR, затем:

```bash
docker-compose up -d
```

Поднимаются: приложение (порт 8080), PostgreSQL, Zookeeper, Kafka, Prometheus (9090), Grafana (3000).

---

## 6. Kafka + ClickHouse

Запуск с ClickHouse:

```bash
docker-compose -f docker-compose.yml -f docker-compose.clickhouse.yml up -d
```

Таблицы ClickHouse нужно создать вручную `kafka-clickhouse/README.md`.

После этого при регистрации покупки и обмене баллов в топик Kafka уходят целые числа ClickHouse читает топик и в materialized view считает суммы положительных и отрицательных.

---
