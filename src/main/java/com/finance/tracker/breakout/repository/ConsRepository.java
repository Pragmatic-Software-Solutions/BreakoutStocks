package com.finance.tracker.breakout.repository;

import com.finance.tracker.breakout.domain.Cons;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsRepository extends JpaRepository<Cons, Long> {
    List<Cons> findByCompanyName(String companyName);
}
