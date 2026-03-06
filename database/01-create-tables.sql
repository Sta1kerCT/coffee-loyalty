-- Клиенты
CREATE TABLE client (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name   VARCHAR2(200) NOT NULL,
    phone       VARCHAR2(50),
    email       VARCHAR2(100),
    balance_points NUMBER(12) DEFAULT 0 NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Награды
CREATE TABLE reward (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR2(200) NOT NULL,
    points_cost NUMBER(10) NOT NULL,
    CONSTRAINT chk_points_cost CHECK (points_cost > 0)
);

-- Покупки
CREATE TABLE purchase (
    id            NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id     NUMBER NOT NULL,
    amount_rub    NUMBER(12,2) NOT NULL,
    points_earned NUMBER(12) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_purchase_client FOREIGN KEY (client_id) REFERENCES client(id),
    CONSTRAINT chk_amount_rub CHECK (amount_rub >= 0),
    CONSTRAINT chk_points_earned CHECK (points_earned >= 0)
);

CREATE INDEX idx_purchase_client ON purchase(client_id);
CREATE INDEX idx_purchase_created ON purchase(created_at);

-- Транзакции баллов
CREATE TABLE point_transaction (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id   NUMBER NOT NULL,
    amount      NUMBER(12) NOT NULL,
    type        VARCHAR2(20) NOT NULL,
    reward_id   NUMBER,
    purchase_id NUMBER,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_pt_client   FOREIGN KEY (client_id)   REFERENCES client(id),
    CONSTRAINT fk_pt_reward   FOREIGN KEY (reward_id)   REFERENCES reward(id),
    CONSTRAINT fk_pt_purchase FOREIGN KEY (purchase_id) REFERENCES purchase(id),
    CONSTRAINT chk_pt_type CHECK (type IN ('PURCHASE', 'REWARD'))
);

CREATE INDEX idx_pt_client ON point_transaction(client_id);
CREATE INDEX idx_pt_created ON point_transaction(created_at);

COMMENT ON TABLE client IS 'Клиенты кофейни';
COMMENT ON TABLE reward IS 'Справочник наград за баллы';
COMMENT ON TABLE purchase IS 'Покупки с начислением баллов';
COMMENT ON TABLE point_transaction IS 'Транзакции баллов (начисление/списание)';
