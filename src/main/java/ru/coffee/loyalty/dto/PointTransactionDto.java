package ru.coffee.loyalty.dto;

import lombok.*;
import ru.coffee.loyalty.entity.PointTransaction;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointTransactionDto {
    private Long id;
    private Long clientId;
    private Integer amount;
    private PointTransaction.TransactionType type;
    private Long rewardId;
    private Long purchaseId;
    private Instant createdAt;
}
