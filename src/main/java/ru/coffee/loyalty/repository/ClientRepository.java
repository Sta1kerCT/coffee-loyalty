package ru.coffee.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.coffee.loyalty.entity.Client;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByFullNameContainingIgnoreCaseOrPhoneContaining(String fullName, String phone);
}
