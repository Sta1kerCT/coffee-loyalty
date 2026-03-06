package ru.coffee.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.coffee.loyalty.entity.Reward;

public interface RewardRepository extends JpaRepository<Reward, Long> {
}
