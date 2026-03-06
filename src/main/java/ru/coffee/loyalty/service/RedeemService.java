package ru.coffee.loyalty.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coffee.loyalty.dto.PointTransactionDto;
import ru.coffee.loyalty.dto.RedeemRequest;
import ru.coffee.loyalty.entity.Client;
import ru.coffee.loyalty.entity.PointTransaction;
import ru.coffee.loyalty.entity.Reward;
import ru.coffee.loyalty.repository.ClientRepository;
import ru.coffee.loyalty.repository.PointTransactionRepository;
import ru.coffee.loyalty.repository.RewardRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedeemService {

    private final ClientRepository clientRepository;
    private final RewardRepository rewardRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final PointTransactionSender pointSender;

    @Transactional
    public PointTransactionDto redeem(RedeemRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + request.getClientId()));
        Reward reward = rewardRepository.findById(request.getRewardId())
                .orElseThrow(() -> new IllegalArgumentException("Reward not found: " + request.getRewardId()));
        int cost = reward.getPointsCost();
        if (client.getBalancePoints() < cost) {
            throw new IllegalStateException("Insufficient balance: " + client.getBalancePoints() + " < " + cost);
        }
        PointTransaction tx = PointTransaction.builder()
                .client(client)
                .amount(-cost)
                .type(PointTransaction.TransactionType.REWARD)
                .reward(reward)
                .build();
        tx = pointTransactionRepository.save(tx);
        client.setBalancePoints(client.getBalancePoints() - cost);
        clientRepository.save(client);
        try {
            pointSender.sendPointsDelta(-cost);
        } catch (Exception e) {
            log.warn("Не удалось отправить дельту баллов в Kafka (обмен выполнен): {}", e.getMessage());
        }
        return toDto(tx);
    }

    private PointTransactionDto toDto(PointTransaction tx) {
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
