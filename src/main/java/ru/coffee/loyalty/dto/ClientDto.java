package ru.coffee.loyalty.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private Long id;
    @NotBlank
    private String fullName;
    private String phone;
    private String email;
    private Integer balancePoints;
    private Instant createdAt;
}
