package com.firstclub.membership.entity;

import com.firstclub.membership.enums.TierType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TierType type;

    private Integer rankValue;

    private Boolean active;
}