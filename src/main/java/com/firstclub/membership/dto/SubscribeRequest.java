package com.firstclub.membership.dto;

import com.firstclub.membership.enums.PlanType;
import com.firstclub.membership.enums.TierType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeRequest {

    @NotNull
    private Long userId;

    @NotNull
    private PlanType planType;

    @NotNull
    private TierType tierType;
}