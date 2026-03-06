package ru.coffee.loyalty.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coffee.loyalty.dto.PointTransactionDto;
import ru.coffee.loyalty.repository.PointTransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointTransactionService {

    private final PointTransactionRepository pointTransactionRepository;

    @Transactional(readOnly = true)
    public List<PointTransactionDto> findByClientId(Long clientId) {
        return pointTransactionRepository.findByClientIdOrderByCreatedAtDesc(clientId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PointTransactionDto getById(Long id) {
        return pointTransactionRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("PointTransaction not found: " + id));
    }

    private PointTransactionDto toDto(ru.coffee.loyalty.entity.PointTransaction tx) {
        return PointTransactionDto.builder()
                .id(tx.getId())
                .clientId(tx.getClient().getId())
                .amount(tx.getAmount())
                .type(tx.getType())
                .rewardId(tx.getReward() != null ? tx.getReward().getId() : null)
                .purchaseId(tx.getPurchase() != null ? tx.getPurchase().getId() : null)
                .createdAt(tx.getCreatedAt())
                .build();
    }
}
