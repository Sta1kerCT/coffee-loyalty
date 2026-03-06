package ru.coffee.loyalty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardDto {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    @Positive
    private Integer pointsCost;
}
