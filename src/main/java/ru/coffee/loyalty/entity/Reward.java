package ru.coffee.loyalty.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reward")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "points_cost", nullable = false)
    private Integer pointsCost;

    @OneToMany(mappedBy = "reward")
    @Builder.Default
    private List<PointTransaction> transactions = new ArrayList<>();
}
