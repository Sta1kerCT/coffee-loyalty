package ru.coffee.loyalty.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.coffee.loyalty.dto.PointTransactionDto;
import ru.coffee.loyalty.dto.RedeemRequest;
import ru.coffee.loyalty.service.RedeemService;

@RestController
@RequestMapping("/api/redeem")
@RequiredArgsConstructor
@CrossOrigin
public class RedeemController {

    private final RedeemService redeemService;

    @PostMapping
    public PointTransactionDto redeem(@Valid @RequestBody RedeemRequest request) {
        return redeemService.redeem(request);
    }
}
