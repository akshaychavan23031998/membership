package com.firstclub.membership.service;

import com.firstclub.membership.dto.SubscribeRequest;
import com.firstclub.membership.dto.SubscriptionResponse;
import com.firstclub.membership.enums.TierType;

public interface SubscriptionService {

    SubscriptionResponse subscribe(SubscribeRequest request);

    SubscriptionResponse getCurrentSubscription(Long userId);

    SubscriptionResponse changeTier(Long userId, TierType tierType);

    void cancelSubscription(Long userId);
}