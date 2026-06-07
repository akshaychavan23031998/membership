package com.firstclub.membership.strategy;

import com.firstclub.membership.entity.TierRule;
import com.firstclub.membership.entity.User;
import com.firstclub.membership.entity.UserOrderStats;
import com.firstclub.membership.enums.RuleType;
import org.springframework.stereotype.Component;

@Component
public class CohortEligibilityStrategy implements TierEligibilityStrategy {

    @Override
    public boolean supports(TierRule rule) {
        return rule.getRuleType() == RuleType.COHORT;
    }

    @Override
    public boolean isEligible(User user, UserOrderStats stats, TierRule rule) {
        return user.getCohort() != null &&
                user.getCohort().equalsIgnoreCase(rule.getRuleValue());
    }
}