## Тема

**Кофейня (система лояльности)** — начисление и списание баллов, награды, покупки.

## Документация
Все лабы связанные с документацией и описанием проекта(1, 2, 4, 7) задачкам лежит в папке `docs`

## Сборка и запуск
`docs/RUN-AND-TEST.md` — как запустить и протестировать

```bash
./mvnw clean package -DskipTests
java -jar target/coffee-loyalty-1.0.0-SNAPSHOT.jar
```

Приложение: http://localhost:8080  
API: http://localhost:8080/api/clients, /api/rewards, /api/purchases, /api/redeem, /api/transactions/...  
UI: http://localhost:8080/  
Actuator (health, prometheus): http://localhost:8080/actuator  

Без Kafka (тесты, dev): `KAFKA_ENABLED=false` или в `application-test.yml` уже отключено.


## Тесты

- **API:** `./mvnw test` — по умолчанию запускаются только API-тесты (UI исключены).
- **Все тесты (включая UI):** `./mvnw test -P ui-tests` — нужен Chrome; при ошибке Netty нужно поиграться с vm options `-Dwebdriver.http.factory=jdk-http-client`.
- **Postman:** импорт `postman/Coffee-Loyalty-API.postman_collection.json`, baseUrl = http://localhost:8080.

## Grafana

1. Запуск: `docker-compose up -d` (app, prometheus, grafana).
2. Grafana: http://localhost:3000 (admin/admin).
3. Datasource Prometheus уже добавлен (provisioning).
4. Алерты: Prometheus `up{job="coffee-loyalty"} == 0`. `monitoring/grafana/alert-rules.yml`.

## Kafka + ClickHouse

- В топик `point-transactions` отправляются сообщения JSON `{"value": N}` (целое число: + начисление, − списание).
- При ошибке отправки — дублирование в `point-transactions-dlq`.
- ClickHouse: таблица с Kafka engine читает топик, materialized view считает сумму положительных и отрицательных. Скрипты в `kafka-clickhouse/`.

Запуск с ClickHouse:
```bash
docker-compose -f docker-compose.yml -f docker-compose.clickhouse.yml up -d
# Инициализация таблиц ClickHouse — выполнить скрипты в kafka-clickhouse/
```
