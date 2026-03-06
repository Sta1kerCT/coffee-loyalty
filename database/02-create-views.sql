-- Клиенты
CREATE OR REPLACE VIEW v_client_stats AS
SELECT c.id,
       c.full_name,
       c.phone,
       c.email,
       c.balance_points,
       c.created_at,
       COUNT(p.id) AS purchase_count,
       MAX(p.created_at) AS last_purchase_at
FROM client c
LEFT JOIN purchase p ON p.client_id = c.id
GROUP BY c.id, c.full_name, c.phone, c.email, c.balance_points, c.created_at;

-- Транзакции баллов
CREATE OR REPLACE VIEW v_point_transaction_detail AS
SELECT pt.id,
       pt.client_id,
       c.full_name AS client_name,
       pt.amount,
       pt.type,
       pt.reward_id,
       r.name AS reward_name,
       pt.purchase_id,
       pt.created_at
FROM point_transaction pt
JOIN client c ON c.id = pt.client_id
LEFT JOIN reward r ON r.id = pt.reward_id;

-- Обмен наград
CREATE OR REPLACE VIEW v_reward_usage AS
SELECT r.id,
       r.name,
       r.points_cost,
       COUNT(pt.id) AS times_redeemed
FROM reward r
LEFT JOIN point_transaction pt ON pt.reward_id = r.id AND pt.type = 'REWARD'
GROUP BY r.id, r.name, r.points_cost;
