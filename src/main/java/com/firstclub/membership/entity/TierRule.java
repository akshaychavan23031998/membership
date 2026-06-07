package com.firstclub.membership.entity;

import com.firstclub.membership.enums.RuleType;
import com.firstclub.membership.enums.TierType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TierRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TierType tierType;

    @Enumerated(EnumType.STRING)
    private RuleType ruleType;

    private String ruleValue;

    private Boolean active;
}