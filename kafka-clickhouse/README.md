# Kafka + ClickHouse: топик с целыми числами, DLQ, materialized view

- В топик `point-transactions` сервис отправляет целые числа (дельта баллов: положительные — начисление, отрицательные — списание).
- При ошибке отправки сообщение дублируется в DLQ `point-transactions-dlq`.
- ClickHouse читает топик через Kafka engine, materialized view считает сумму положительных и отрицательных.

Запуск: `docker-compose.clickhouse.yml`.
