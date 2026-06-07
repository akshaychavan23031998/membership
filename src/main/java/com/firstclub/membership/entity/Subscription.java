package com.firstclub.membership.entity;

import com.firstclub.membership.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private MembershipPlan plan;

    @ManyToOne
    private MembershipTier tier;

    private LocalDateTime startDate;

    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Version
    private Long version;
}