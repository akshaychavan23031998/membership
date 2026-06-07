package com.firstclub.membership.controller;

import com.firstclub.membership.dto.ChangeTierRequest;
import com.firstclub.membership.dto.EligibilityResponse;
import com.firstclub.membership.dto.SubscribeRequest;
import com.firstclub.membership.dto.SubscriptionResponse;
import com.firstclub.membership.entity.MembershipPlan;
import com.firstclub.membership.entity.MembershipTier;
import com.firstclub.membership.entity.TierBenefit;
import com.firstclub.membership.exception.ResourceNotFoundException;
import com.firstclub.membership.repository.MembershipPlanRepository;
import com.firstclub.membership.repository.MembershipTierRepository;
import com.firstclub.membership.repository.TierBenefitRepository;
import com.firstclub.membership.service.SubscriptionService;
import com.firstclub.membership.service.TierEvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipPlanRepository planRepository;
    private final MembershipTierRepository tierRepository;
    private final TierBenefitRepository benefitRepository;
    private final SubscriptionService subscriptionService;
    private final TierEvaluationService tierEvaluationService;

    @GetMapping("/plans")
    public List<MembershipPlan> getPlans() {
        return planRepository.findAll();
    }

    @GetMapping("/tiers")
    public List<MembershipTier> getTiers() {
        return tierRepository.findAll();
    }

    @GetMapping("/tiers/{tierId}/benefits")
    public List<TierBenefit> getTierBenefits(@PathVariable Long tierId) {
        MembershipTier tier = tierRepository.findById(tierId)
                .orElseThrow(() -> new ResourceNotFoundException("Tier not found"));

        return benefitRepository.findByTierAndActiveTrue(tier);
    }

    @PostMapping("/subscribe")
    public SubscriptionResponse subscribe(@Valid @RequestBody SubscribeRequest request) {
        return subscriptionService.subscribe(request);
    }

    @GetMapping("/users/{userId}/subscription")
    public SubscriptionResponse getCurrentSubscription(@PathVariable Long userId) {
        return subscriptionService.getCurrentSubscription(userId);
    }

    @PutMapping("/users/{userId}/tier")
    public SubscriptionResponse changeTier(
            @PathVariable Long userId,
            @Valid @RequestBody ChangeTierRequest request
    ) {
        return subscriptionService.changeTier(userId, request.getTierType());
    }

    @DeleteMapping("/users/{userId}/subscription")
    public String cancelSubscription(@PathVariable Long userId) {
        subscriptionService.cancelSubscription(userId);
        return "Subscription cancelled successfully";
    }

    @GetMapping("/users/{userId}/tier-eligibility")
    public EligibilityResponse evaluateTier(@PathVariable Long userId) {
        return tierEvaluationService.evaluateUserTier(userId);
    }
}