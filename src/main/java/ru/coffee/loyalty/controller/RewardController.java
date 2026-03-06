package ru.coffee.loyalty.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.coffee.loyalty.dto.RewardDto;
import ru.coffee.loyalty.service.RewardService;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
@CrossOrigin
public class RewardController {

    private final RewardService rewardService;

    @GetMapping
    public List<RewardDto> list() {
        return rewardService.findAll();
    }

    @GetMapping("/{id}")
    public RewardDto get(@PathVariable Long id) {
        return rewardService.getById(id);
    }

    @PostMapping
    public ResponseEntity<RewardDto> create(@Valid @RequestBody RewardDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rewardService.create(dto));
    }

    @PutMapping("/{id}")
    public RewardDto update(@PathVariable Long id, @Valid @RequestBody RewardDto dto) {
        return rewardService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rewardService.delete(id);
    }
}
