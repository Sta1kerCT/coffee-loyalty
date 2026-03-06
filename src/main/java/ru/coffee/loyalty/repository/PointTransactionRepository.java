package ru.coffee.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.coffee.loyalty.entity.PointTransaction;

import java.util.List;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {

    List<PointTransaction> findByClientIdOrderByCreatedAtDesc(Long clientId);
}
