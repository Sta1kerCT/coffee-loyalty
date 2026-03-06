package ru.coffee.loyalty.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coffee.loyalty.dto.ClientDto;
import ru.coffee.loyalty.entity.Client;
import ru.coffee.loyalty.repository.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public List<ClientDto> findAll(String search) {
        if (search != null && !search.isBlank()) {
            return clientRepository.findByFullNameContainingIgnoreCaseOrPhoneContaining(search, search)
                    .stream().map(this::toDto).collect(Collectors.toList());
        }
        return clientRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClientDto getById(Long id) {
        return clientRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));
    }

    @Transactional
    public ClientDto create(ClientDto dto) {
        Client e = toEntity(dto);
        e.setId(null);
        e.setBalancePoints(0);
        return toDto(clientRepository.save(e));
    }

    @Transactional
    public ClientDto update(Long id, ClientDto dto) {
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));
        existing.setFullName(dto.getFullName());
        existing.setPhone(dto.getPhone());
        existing.setEmail(dto.getEmail());
        return toDto(clientRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) throw new IllegalArgumentException("Client not found: " + id);
        clientRepository.deleteById(id);
    }

    private ClientDto toDto(Client e) {
        return ClientDto.builder()
                .id(e.getId())
                .fullName(e.getFullName())
                .phone(e.getPhone())
                .email(e.getEmail())
                .balancePoints(e.getBalancePoints())
                .createdAt(e.getCreatedAt())
                .build();
    }

    private Client toEntity(ClientDto dto) {
        return Client.builder()
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .balancePoints(dto.getBalancePoints() != null ? dto.getBalancePoints() : 0)
                .build();
    }
}
