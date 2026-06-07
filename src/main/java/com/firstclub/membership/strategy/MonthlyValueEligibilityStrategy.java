package com.firstclub.membership.strategy;

import com.firstclub.membership.entity.TierRule;
import com.firstclub.membership.entity.User;
import com.firstclub.membership.entity.UserOrderStats;
import com.firstclub.membership.enums.RuleType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MonthlyValueEligibilityStrategy implements TierEligibilityStrategy {

    @Override
    public boolean supports(TierRule rule) {
        return rule.getRuleType() == RuleType.MONTHLY_ORDER_VALUE;
    }

    @Override
    public boolean isEligible(User user, UserOrderStats stats, TierRule rule) {
        BigDecimal requiredValue = new BigDecimal(rule.getRuleValue());
        return stats != null && stats.getTotalOrderValue().compareTo(requiredValue) >= 0;
    }
}