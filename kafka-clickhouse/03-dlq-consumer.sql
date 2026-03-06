-- Чтение DLQ топика (point-transactions-dlq) в отдельную таблицу для разбора ошибок

CREATE TABLE IF NOT EXISTS point_transactions_dlq_queue (
    key String,
    value Int32,
    _raw String,
    _timestamp DateTime
) ENGINE = Kafka
SETTINGS
    kafka_broker_list = 'kafka:9092',
    kafka_topic_list = 'point-transactions-dlq',
    kafka_group_name = 'clickhouse-dlq-consumer',
    kafka_format = 'JSONEachRow';

CREATE TABLE IF NOT EXISTS point_transactions_dlq (
    key String,
    value Int32,
    created_at DateTime DEFAULT now()
) ENGINE = MergeTree()
ORDER BY created_at;

CREATE MATERIALIZED VIEW IF NOT EXISTS point_transactions_dlq_mv TO point_transactions_dlq AS
SELECT key, value, now() AS created_at FROM point_transactions_dlq_queue;
