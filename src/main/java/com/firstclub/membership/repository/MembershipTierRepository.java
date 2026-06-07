package com.firstclub.membership.repository;

import com.firstclub.membership.entity.MembershipTier;
import com.firstclub.membership.enums.TierType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipTierRepository extends JpaRepository<MembershipTier, Long> {

    Optional<MembershipTier> findByTypeAndActiveTrue(TierType type);
}