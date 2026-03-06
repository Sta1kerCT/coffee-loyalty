package ru.coffee.loyalty.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedeemRequest {
    @NotNull
    private Long clientId;
    @NotNull
    private Long rewardId;
}
