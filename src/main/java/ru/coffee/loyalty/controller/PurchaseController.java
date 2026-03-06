package ru.coffee.loyalty.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.coffee.loyalty.dto.PurchaseDto;
import ru.coffee.loyalty.service.PurchaseService;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
@CrossOrigin
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public List<PurchaseDto> list() {
        return purchaseService.findAll();
    }

    @GetMapping("/client/{clientId}")
    public List<PurchaseDto> byClient(@PathVariable Long clientId) {
        return purchaseService.findByClientId(clientId);
    }

    @GetMapping("/{id}")
    public PurchaseDto get(@PathVariable Long id) {
        return purchaseService.getById(id);
    }

    @PostMapping
    public ResponseEntity<PurchaseDto> create(@Valid @RequestBody PurchaseDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.create(dto));
    }
}
