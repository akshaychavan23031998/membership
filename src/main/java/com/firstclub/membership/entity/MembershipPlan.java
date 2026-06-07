package com.firstclub.membership.entity;

import com.firstclub.membership.enums.PlanType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlanType type;

    private BigDecimal price;

    private Integer durationInDays;

    private Boolean active;
}