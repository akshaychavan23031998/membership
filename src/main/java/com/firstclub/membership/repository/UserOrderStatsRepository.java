package com.firstclub.membership.repository;

import com.firstclub.membership.entity.UserOrderStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOrderStatsRepository extends JpaRepository<UserOrderStats, Long> {

    Optional<UserOrderStats> findByUserIdAndMonth(Long userId, String month);
}