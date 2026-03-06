package ru.coffee.loyalty.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coffee.loyalty.dto.RewardDto;
import ru.coffee.loyalty.entity.Reward;
import ru.coffee.loyalty.repository.RewardRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;

    @Transactional(readOnly = true)
    public List<RewardDto> findAll() {
        return rewardRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RewardDto getById(Long id) {
        return rewardRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Reward not found: " + id));
    }

    @Transactional
    public RewardDto create(RewardDto dto) {
        Reward e = toEntity(dto);
        e.setId(null);
        return toDto(rewardRepository.save(e));
    }

    @Transactional
    public RewardDto update(Long id, RewardDto dto) {
        Reward existing = rewardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reward not found: " + id));
        existing.setName(dto.getName());
        existing.setPointsCost(dto.getPointsCost());
        return toDto(rewardRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!rewardRepository.existsById(id)) throw new IllegalArgumentException("Reward not found: " + id);
        rewardRepository.deleteById(id);
    }

    private RewardDto toDto(Reward e) {
        return RewardDto.builder()
                .id(e.getId())
                .name(e.getName())
                .pointsCost(e.getPointsCost())
                .build();
    }

    private Reward toEntity(RewardDto dto) {
        return Reward.builder()
                .name(dto.getName())
                .pointsCost(dto.getPointsCost())
                .build();
    }
}
