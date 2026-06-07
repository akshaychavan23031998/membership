package com.firstclub.membership.dto;

import com.firstclub.membership.enums.PlanType;
import com.firstclub.membership.enums.SubscriptionStatus;
import com.firstclub.membership.enums.TierType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SubscriptionResponse {

    private Long subscriptionId;
    private Long userId;
    private PlanType planType;
    private TierType tierType;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
    private SubscriptionStatus status;
}