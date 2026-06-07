package com.firstclub.membership.repository;

import com.firstclub.membership.entity.TierRule;
import com.firstclub.membership.enums.TierType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TierRuleRepository extends JpaRepository<TierRule, Long> {

    List<TierRule> findByTierTypeAndActiveTrue(TierType tierType);
}