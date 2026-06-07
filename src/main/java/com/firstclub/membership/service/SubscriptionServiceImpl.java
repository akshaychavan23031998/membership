package com.firstclub.membership.service;

import com.firstclub.membership.dto.SubscribeRequest;
import com.firstclub.membership.dto.SubscriptionResponse;
import com.firstclub.membership.entity.MembershipPlan;
import com.firstclub.membership.entity.MembershipTier;
import com.firstclub.membership.entity.Subscription;
import com.firstclub.membership.entity.User;
import com.firstclub.membership.enums.SubscriptionStatus;
import com.firstclub.membership.enums.TierType;
import com.firstclub.membership.exception.BadRequestException;
import com.firstclub.membership.exception.ResourceNotFoundException;
import com.firstclub.membership.repository.MembershipPlanRepository;
import com.firstclub.membership.repository.MembershipTierRepository;
import com.firstclub.membership.repository.SubscriptionRepository;
import com.firstclub.membership.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepository userRepository;
    private final MembershipPlanRepository planRepository;
    private final MembershipTierRepository tierRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public SubscriptionResponse subscribe(SubscribeRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        subscriptionRepository
                .findByUserIdAndStatus(user.getId(), SubscriptionStatus.ACTIVE)
                .ifPresent(subscription -> {
                    throw new BadRequestException("User already has an active subscription");
                });

        MembershipPlan plan = planRepository.findByTypeAndActiveTrue(request.getPlanType())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        MembershipTier tier = tierRepository.findByTypeAndActiveTrue(request.getTierType())
                .orElseThrow(() -> new ResourceNotFoundException("Tier not found"));

        LocalDateTime now = LocalDateTime.now();

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .tier(tier)
                .startDate(now)
                .expiryDate(now.plusDays(plan.getDurationInDays()))
                .status(SubscriptionStatus.ACTIVE)
                .build();

        Subscription saved = subscriptionRepository.save(subscription);

        return mapToResponse(saved);
    }

    @Override
    public SubscriptionResponse getCurrentSubscription(Long userId) {
        Subscription subscription = subscriptionRepository
                .findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active subscription not found"));

        return mapToResponse(subscription);
    }

    @Override
    @Transactional
    public SubscriptionResponse changeTier(Long userId, TierType tierType) {
        Subscription subscription = subscriptionRepository
                .findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active subscription not found"));

        MembershipTier newTier = tierRepository.findByTypeAndActiveTrue(tierType)
                .orElseThrow(() -> new ResourceNotFoundException("Tier not found"));

        subscription.setTier(newTier);

        Subscription saved = subscriptionRepository.save(subscription);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public void cancelSubscription(Long userId) {
        Subscription subscription = subscriptionRepository
                .findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active subscription not found"));

        subscription.setStatus(SubscriptionStatus.CANCELLED);

        subscriptionRepository.save(subscription);
    }

    private SubscriptionResponse mapToResponse(Subscription subscription) {
        return SubscriptionResponse.builder()
                .subscriptionId(subscription.getId())
                .userId(subscription.getUser().getId())
                .planType(subscription.getPlan().getType())
                .tierType(subscription.getTier().getType())
                .startDate(subscription.getStartDate())
                .expiryDate(subscription.getExpiryDate())
                .status(subscription.getStatus())
                .build();
    }
}