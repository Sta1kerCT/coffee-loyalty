package ru.coffee.loyalty.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.coffee.loyalty.dto.PointTransactionDto;
import ru.coffee.loyalty.service.PointTransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin
public class PointTransactionController {

    private final PointTransactionService pointTransactionService;

    @GetMapping("/client/{clientId}")
    public List<PointTransactionDto> byClient(@PathVariable Long clientId) {
        return pointTransactionService.findByClientId(clientId);
    }

    @GetMapping("/{id}")
    public PointTransactionDto get(@PathVariable Long id) {
        return pointTransactionService.getById(id);
    }
}
