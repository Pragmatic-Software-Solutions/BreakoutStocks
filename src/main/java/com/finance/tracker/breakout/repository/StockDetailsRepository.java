package com.finance.tracker.breakout.repository;

import com.finance.tracker.breakout.domain.StockDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StockDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockDetailsRepository extends JpaRepository<StockDetails, Long> {}
