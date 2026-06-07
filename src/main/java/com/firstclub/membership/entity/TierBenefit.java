package com.firstclub.membership.entity;

import com.firstclub.membership.enums.BenefitType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TierBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MembershipTier tier;

    @Enumerated(EnumType.STRING)
    private BenefitType benefitType;

    private String benefitValue;

    private Boolean active;
}