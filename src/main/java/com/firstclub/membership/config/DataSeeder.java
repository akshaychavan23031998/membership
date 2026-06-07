package com.firstclub.membership.config;

import com.firstclub.membership.entity.MembershipPlan;
import com.firstclub.membership.entity.MembershipTier;
import com.firstclub.membership.entity.TierBenefit;
import com.firstclub.membership.entity.TierRule;
import com.firstclub.membership.entity.User;
import com.firstclub.membership.entity.UserOrderStats;
import com.firstclub.membership.enums.BenefitType;
import com.firstclub.membership.enums.PlanType;
import com.firstclub.membership.enums.RuleType;
import com.firstclub.membership.enums.TierType;
import com.firstclub.membership.repository.MembershipPlanRepository;
import com.firstclub.membership.repository.MembershipTierRepository;
import com.firstclub.membership.repository.TierBenefitRepository;
import com.firstclub.membership.repository.TierRuleRepository;
import com.firstclub.membership.repository.UserOrderStatsRepository;
import com.firstclub.membership.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MembershipPlanRepository planRepository;
    private final MembershipTierRepository tierRepository;
    private final TierBenefitRepository benefitRepository;
    private final TierRuleRepository tierRuleRepository;
    private final UserOrderStatsRepository statsRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User user1 = userRepository.save(User.builder()
                .name("Rahul")
                .email("rahul@example.com")
                .cohort("PREMIUM_CUSTOMERS")
                .build());

        User user2 = userRepository.save(User.builder()
                .name("Normal User")
                .email("normal@example.com")
                .cohort("REGULAR")
                .build());

        planRepository.save(MembershipPlan.builder()
                .type(PlanType.MONTHLY)
                .price(new BigDecimal("299"))
                .durationInDays(30)
                .active(true)
                .build());

        planRepository.save(MembershipPlan.builder()
                .type(PlanType.QUARTERLY)
                .price(new BigDecimal("799"))
                .durationInDays(90)
                .active(true)
                .build());

        planRepository.save(MembershipPlan.builder()
                .type(PlanType.YEARLY)
                .price(new BigDecimal("2499"))
                .durationInDays(365)
                .active(true)
                .build());

        MembershipTier silver = tierRepository.save(MembershipTier.builder()
                .type(TierType.SILVER)
                .rankValue(1)
                .active(true)
                .build());

        MembershipTier gold = tierRepository.save(MembershipTier.builder()
                .type(TierType.GOLD)
                .rankValue(2)
                .active(true)
                .build());

        MembershipTier platinum = tierRepository.save(MembershipTier.builder()
                .type(TierType.PLATINUM)
                .rankValue(3)
                .active(true)
                .build());

        benefitRepository.save(TierBenefit.builder()
                .tier(silver)
                .benefitType(BenefitType.FREE_DELIVERY)
                .benefitValue("Eligible orders above ₹499")
                .active(true)
                .build());

        benefitRepository.save(TierBenefit.builder()
                .tier(gold)
                .benefitType(BenefitType.DISCOUNT)
                .benefitValue("10% discount on selected categories")
                .active(true)
                .build());

        benefitRepository.save(TierBenefit.builder()
                .tier(platinum)
                .benefitType(BenefitType.PRIORITY_SUPPORT)
                .benefitValue("24x7 priority support")
                .active(true)
                .build());

        benefitRepository.save(TierBenefit.builder()
                .tier(platinum)
                .benefitType(BenefitType.EXCLUSIVE_COUPONS)
                .benefitValue("PLATINUM20")
                .active(true)
                .build());

        tierRuleRepository.save(TierRule.builder()
                .tierType(TierType.GOLD)
                .ruleType(RuleType.ORDER_COUNT)
                .ruleValue("5")
                .active(true)
                .build());

        tierRuleRepository.save(TierRule.builder()
                .tierType(TierType.GOLD)
                .ruleType(RuleType.COHORT)
                .ruleValue("PREMIUM_CUSTOMERS")
                .active(true)
                .build());

        tierRuleRepository.save(TierRule.builder()
                .tierType(TierType.PLATINUM)
                .ruleType(RuleType.MONTHLY_ORDER_VALUE)
                .ruleValue("10000")
                .active(true)
                .build());

        statsRepository.save(UserOrderStats.builder()
                .userId(user1.getId())
                .orderCount(6)
                .totalOrderValue(new BigDecimal("12000"))
                .month(YearMonth.now().toString())
                .build());

        statsRepository.save(UserOrderStats.builder()
                .userId(user2.getId())
                .orderCount(2)
                .totalOrderValue(new BigDecimal("1500"))
                .month(YearMonth.now().toString())
                .build());
    }
}