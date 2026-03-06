package ru.coffee.loyalty.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDto {
    private Long id;
    @NotNull
    private Long clientId;
    @NotNull
    @Positive
    private BigDecimal amountRub;
    private Integer pointsEarned;
    private Instant createdAt;
}
