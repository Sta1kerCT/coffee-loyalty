package ru.coffee.loyalty.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coffee.loyalty.dto.PurchaseDto;
import ru.coffee.loyalty.entity.Client;
import ru.coffee.loyalty.entity.PointTransaction;
import ru.coffee.loyalty.entity.Purchase;
import ru.coffee.loyalty.repository.ClientRepository;
import ru.coffee.loyalty.repository.PurchaseRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ClientRepository clientRepository;
    private final PointTransactionSender pointSender;

    @Value("${app.points-per-rub:0.1}")
    private double pointsPerRub;

    @Transactional(readOnly = true)
    public List<PurchaseDto> findAll() {
        return purchaseRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PurchaseDto> findByClientId(Long clientId) {
        return purchaseRepository.findByClientIdOrderByCreatedAtDesc(clientId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PurchaseDto getById(Long id) {
        return purchaseRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found: " + id));
    }

    @Transactional
    public PurchaseDto create(PurchaseDto dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + dto.getClientId()));
        int points = dto.getPointsEarned() != null ? dto.getPointsEarned() : calculatePoints(dto.getAmountRub());
        Purchase p = Purchase.builder()
                .client(client)
                .amountRub(dto.getAmountRub())
                .pointsEarned(points)
                .build();
        p = purchaseRepository.save(p);
        PointTransaction tx = PointTransaction.builder()
                .client(client)
                .amount(points)
                .type(PointTransaction.TransactionType.PURCHASE)
                .purchase(p)
                .build();
        tx.setClient(client);
        tx.setPurchase(p);
        client.getTransactions().add(tx);
        client.setBalancePoints(client.getBalancePoints() + points);
        clientRepository.save(client);
        try {
            pointSender.sendPointsDelta(points);
        } catch (Exception e) {
            log.warn("Не удалось отправить дельту баллов в Kafka (покупка сохранена): {}", e.getMessage());
        }
        return toDto(p);
    }

    private int calculatePoints(BigDecimal amountRub) {
        return amountRub.multiply(BigDecimal.valueOf(pointsPerRub)).setScale(0, RoundingMode.DOWN).intValue();
    }

    private PurchaseDto toDto(Purchase p) {
        return PurchaseDto.builder()
                .id(p.getId())
                .clientId(p.getClient().getId())
                .amountRub(p.getAmountRub())
                .pointsEarned(p.getPointsEarned())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
