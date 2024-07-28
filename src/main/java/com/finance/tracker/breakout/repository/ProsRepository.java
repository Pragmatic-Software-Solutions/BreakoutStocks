package com.finance.tracker.breakout.repository;

import com.finance.tracker.breakout.domain.Pros;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProsRepository extends JpaRepository<Pros, Long> {
    List<Pros> findByCompanyName(String companyName);
}
