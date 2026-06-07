package com.firstclub.membership.repository;

import com.firstclub.membership.entity.Subscription;
import com.firstclub.membership.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);
}