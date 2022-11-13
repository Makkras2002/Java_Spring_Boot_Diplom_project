package com.makkras.shop.repo;

import com.makkras.shop.entity.CompleteStockRefillOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StockRefillOrdersJpaRepository extends JpaRepository<CompleteStockRefillOrder,Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE complete_stock_refill_orders SET is_completed=? WHERE complete_stock_refill_order_id=?",nativeQuery = true)
    void updateStockRefillOrderStatus(boolean newStatus, Long orderId);
}
