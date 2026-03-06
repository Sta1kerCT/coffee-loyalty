CREATE TABLE IF NOT EXISTS point_transactions_queue (
    value Int32
) ENGINE = Kafka
SETTINGS
    kafka_broker_list = 'kafka:9092',
    kafka_topic_list = 'point-transactions',
    kafka_group_name = 'clickhouse-point-consumer',
    kafka_format = 'JSONEachRow',
    kafka_num_consumers = 1;

CREATE TABLE IF NOT EXISTS point_transactions (
    value Int32,
    created_at DateTime DEFAULT now()
) ENGINE = MergeTree()
ORDER BY created_at;

CREATE MATERIALIZED VIEW IF NOT EXISTS point_transactions_mv TO point_transactions AS
SELECT
    value,
    now() AS created_at
FROM point_transactions_queue;
