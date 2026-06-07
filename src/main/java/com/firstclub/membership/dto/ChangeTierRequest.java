package com.firstclub.membership.dto;

import com.firstclub.membership.enums.TierType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeTierRequest {

    @NotNull
    private TierType tierType;
}