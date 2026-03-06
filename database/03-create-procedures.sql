-- Процедуры для системы лояльности кофейни (Oracle PL/SQL)

-- Начисление баллов за покупку и обновление баланса клиента
CREATE OR REPLACE PROCEDURE proc_register_purchase (
    p_client_id    IN NUMBER,
    p_amount_rub   IN NUMBER,
    p_points_earned IN NUMBER,
    p_purchase_id  OUT NUMBER
) IS
BEGIN
    INSERT INTO purchase (client_id, amount_rub, points_earned)
    VALUES (p_client_id, p_amount_rub, p_points_earned)
    RETURNING id INTO p_purchase_id;

    INSERT INTO point_transaction (client_id, amount, type, purchase_id)
    VALUES (p_client_id, p_points_earned, 'PURCHASE', p_purchase_id);

    UPDATE client
    SET balance_points = balance_points + p_points_earned
    WHERE id = p_client_id;

    COMMIT;
END;
/

-- Обмен баллов на награду (списание)
CREATE OR REPLACE PROCEDURE proc_redeem_reward (
    p_client_id IN NUMBER,
    p_reward_id IN NUMBER,
    p_success   OUT NUMBER
) IS
    v_cost    NUMBER;
    v_balance NUMBER;
BEGIN
    p_success := 0;

    SELECT points_cost INTO v_cost FROM reward WHERE id = p_reward_id;
    SELECT balance_points INTO v_balance FROM client WHERE id = p_client_id;

    IF v_balance < v_cost THEN
        RAISE_APPLICATION_ERROR(-20001, 'Insufficient balance');
    END IF;

    INSERT INTO point_transaction (client_id, amount, type, reward_id)
    VALUES (p_client_id, -v_cost, 'REWARD', p_reward_id);

    UPDATE client
    SET balance_points = balance_points - v_cost
    WHERE id = p_client_id;

    p_success := 1;
    COMMIT;
END;
/

-- Пересчёт баланса клиента по транзакциям (корректирующая процедура)
CREATE OR REPLACE PROCEDURE proc_recalc_client_balance (p_client_id IN NUMBER) IS
    v_sum NUMBER;
BEGIN
    SELECT NVL(SUM(amount), 0) INTO v_sum
    FROM point_transaction
    WHERE client_id = p_client_id;

    UPDATE client SET balance_points = v_sum WHERE id = p_client_id;
    COMMIT;
END;
/
