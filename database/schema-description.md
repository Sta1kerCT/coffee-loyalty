# Схема БД

## Диаграмма

```mermaid
erDiagram
    CLIENT {
        number id PK
        string full_name
        string phone
        string email
        number balance_points
        timestamp created_at
    }
    REWARD {
        number id PK
        string name
        number points_cost
    }
    PURCHASE {
        number id PK
        number client_id FK
        number amount_rub
        number points_earned
        timestamp created_at
    }
    POINT_TRANSACTION {
        number id PK
        number client_id FK
        number amount
        string type
        number reward_id FK
        number purchase_id FK
        timestamp created_at
    }
    CLIENT ||--o{ PURCHASE : "покупки"
    CLIENT ||--o{ POINT_TRANSACTION : "транзакции"
    REWARD ||--o{ POINT_TRANSACTION : "обмен на награду"
    PURCHASE ||--o{ POINT_TRANSACTION : "начисление баллов"
```

## Таблицы

- **CLIENT** — клиенты: id, full_name, phone, email, balance_points, created_at
- **REWARD** — справочник наград: id, name, points_cost
- **POINT_TRANSACTION** — транзакции баллов: id, client_id, amount, type (PURCHASE/REWARD), reward_id (nullable), purchase_id (nullable), created_at
- **PURCHASE** — покупки: id, client_id, amount_rub, points_earned, created_at

## Связи

- CLIENT.id → POINT_TRANSACTION.client_id (FK)
- CLIENT.id → PURCHASE.client_id (FK)
- REWARD.id → POINT_TRANSACTION.reward_id (FK, nullable)
- PURCHASE.id → POINT_TRANSACTION.purchase_id (FK, nullable)

## Представления

- **v_client_stats** — клиенты с количеством покупок и датой последней покупки
- **v_point_transaction_detail** — транзакции баллов с именем клиента и названием награды
- **v_reward_usage** — награды и количество обменов по каждой
