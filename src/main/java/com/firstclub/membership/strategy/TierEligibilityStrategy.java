package com.firstclub.membership.strategy;

import com.firstclub.membership.entity.TierRule;
import com.firstclub.membership.entity.User;
import com.firstclub.membership.entity.UserOrderStats;

public interface TierEligibilityStrategy {

    boolean supports(TierRule rule);

    boolean isEligible(User user, UserOrderStats stats, TierRule rule);
}