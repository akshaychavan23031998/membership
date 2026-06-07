package com.firstclub.membership.repository;

import com.firstclub.membership.entity.MembershipTier;
import com.firstclub.membership.entity.TierBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TierBenefitRepository extends JpaRepository<TierBenefit, Long> {

    List<TierBenefit> findByTierAndActiveTrue(MembershipTier tier);
}