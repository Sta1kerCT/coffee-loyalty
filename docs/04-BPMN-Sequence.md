# BPMN и Sequence диаграммы по ТЗ

Диаграммы построены на **Mermaid**

---

## BPMN: Регистрация покупки и начисление баллов

```mermaid
flowchart LR
    Start([Старт]) --> A[Принять данные покупки<br/>клиент, сумма]
    A --> B[Рассчитать баллы]
    B --> C[Записать покупку в БД]
    C --> D[Создать транзакцию баллов +]
    D --> E[Обновить баланс клиента]
    E --> F[Отправить amount в Kafka]
    F --> End([Конец])
```

---

## BPMN: Обмен баллов на награду

```mermaid
flowchart LR
    Start([Старт]) --> A[Выбор клиента и награды]
    A --> B{Баланс >=<br/>стоимость?}
    B -->|Да| C[Создать транзакцию -]
    C --> D[Обновить баланс]
    D --> E[Отправить -amount в Kafka]
    E --> End1([Конец])
    B -->|Нет| F[Сообщение:<br/>Недостаточно баллов]
    F --> End2([Конец])
```

---

## Sequence: Регистрация покупки

```mermaid
sequenceDiagram
    participant UI as Клиент (UI)
    participant API as API
    participant Svc as Сервис
    participant DB as БД
    participant K as Kafka

    UI->>API: POST /api/purchases { clientId, amountRub }
    API->>Svc: create(dto)
    Svc->>DB: INSERT purchase
    DB-->>Svc: id
    Svc->>DB: INSERT point_transaction
    Svc->>DB: UPDATE client.balance_points
    Svc->>K: send(points_earned)
    Svc-->>API: PurchaseDto
    API-->>UI: 201 Created
```

---

## Sequence: Обмен баллов на награду

```mermaid
sequenceDiagram
    participant UI as Клиент (UI)
    participant API as API
    participant Svc as Сервис
    participant DB as БД
    participant K as Kafka

    UI->>API: POST /api/redeem { clientId, rewardId }
    API->>Svc: redeem(request)
    Svc->>DB: SELECT balance, cost
    DB-->>Svc: данные
    alt Баланс >= стоимость
        Svc->>DB: INSERT point_transaction (отрицательная)
        Svc->>DB: UPDATE client.balance_points
        Svc->>K: send(-cost)
        Svc-->>API: PointTransactionDto
        API-->>UI: 200 OK
    else Недостаточно баллов
        Svc-->>API: IllegalStateException
        API-->>UI: 400 Bad Request
    end
```

---
