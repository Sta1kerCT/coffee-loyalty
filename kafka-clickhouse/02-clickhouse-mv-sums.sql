CREATE TABLE IF NOT EXISTS point_sums (
    k UInt8 DEFAULT 1,
    sum_positive Int64,
    sum_negative Int64,
    updated_at DateTime DEFAULT now()
) ENGINE = SummingMergeTree()
ORDER BY k;

CREATE MATERIALIZED VIEW IF NOT EXISTS point_sums_mv TO point_sums AS
SELECT
    1 AS k,
    if(value > 0, value, 0) AS sum_positive,
    if(value < 0, value, 0) AS sum_negative,
    now() AS updated_at
FROM point_transactions_queue;

-- SELECT sum(sum_positive) AS total_positive, sum(sum_negative) AS total_negative FROM point_sums;
