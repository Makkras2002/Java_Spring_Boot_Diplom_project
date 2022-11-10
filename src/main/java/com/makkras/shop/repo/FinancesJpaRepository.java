package com.makkras.shop.repo;

import com.makkras.shop.entity.CurrentFinances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface FinancesJpaRepository extends JpaRepository<CurrentFinances,Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE current_finances SET finances_amount = ? WHERE finances_id = ?", nativeQuery = true)
    void updateCurrentFinances(BigDecimal newFinances, Long financesId);
}
