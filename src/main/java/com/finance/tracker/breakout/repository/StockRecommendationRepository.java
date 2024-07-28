package com.finance.tracker.breakout.repository;

import com.finance.tracker.breakout.domain.StockRecommendation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StockRecommendation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRecommendationRepository extends JpaRepository<StockRecommendation, Long> {}
