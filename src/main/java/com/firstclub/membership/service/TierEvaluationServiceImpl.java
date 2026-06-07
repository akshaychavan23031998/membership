package com.firstclub.membership.service;

import com.firstclub.membership.dto.EligibilityResponse;
import com.firstclub.membership.entity.TierRule;
import com.firstclub.membership.entity.User;
import com.firstclub.membership.entity.UserOrderStats;
import com.firstclub.membership.enums.TierType;
import com.firstclub.membership.exception.ResourceNotFoundException;
import com.firstclub.membership.repository.TierRuleRepository;
import com.firstclub.membership.repository.UserOrderStatsRepository;
import com.firstclub.membership.repository.UserRepository;
import com.firstclub.membership.strategy.TierEligibilityStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TierEvaluationServiceImpl implements TierEvaluationService {

    private final UserRepository userRepository;
    private final UserOrderStatsRepository statsRepository;
    private final TierRuleRepository tierRuleRepository;
    private final List<TierEligibilityStrategy> strategies;

    @Override
    public EligibilityResponse evaluateUserTier(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String currentMonth = YearMonth.now().toString();

        UserOrderStats stats = statsRepository
                .findByUserIdAndMonth(userId, currentMonth)
                .orElse(null);

        if (isEligibleForTier(user, stats, TierType.PLATINUM)) {
            return EligibilityResponse.builder()
                    .userId(userId)
                    .eligibleTier(TierType.PLATINUM)
                    .reason("User matched Platinum tier rules")
                    .build();
        }

        if (isEligibleForTier(user, stats, TierType.GOLD)) {
            return EligibilityResponse.builder()
                    .userId(userId)
                    .eligibleTier(TierType.GOLD)
                    .reason("User matched Gold tier rules")
                    .build();
        }

        return EligibilityResponse.builder()
                .userId(userId)
                .eligibleTier(TierType.SILVER)
                .reason("Default tier")
                .build();
    }

    private boolean isEligibleForTier(User user, UserOrderStats stats, TierType tierType) {
        List<TierRule> rules = tierRuleRepository.findByTierTypeAndActiveTrue(tierType);

        return rules.stream().anyMatch(rule ->
                strategies.stream()
                        .filter(strategy -> strategy.supports(rule))
                        .findFirst()
                        .map(strategy -> strategy.isEligible(user, stats, rule))
                        .orElse(false)
        );
    }
}