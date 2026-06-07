package com.firstclub.membership.dto;

import com.firstclub.membership.enums.TierType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EligibilityResponse {

    private Long userId;
    private TierType eligibleTier;
    private String reason;
}