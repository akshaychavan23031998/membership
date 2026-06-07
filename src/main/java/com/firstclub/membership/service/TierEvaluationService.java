package com.firstclub.membership.service;

import com.firstclub.membership.dto.EligibilityResponse;

public interface TierEvaluationService {

    EligibilityResponse evaluateUserTier(Long userId);
}