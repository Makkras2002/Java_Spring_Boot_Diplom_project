package com.makkras.shop.repo;

import com.makkras.shop.entity.CompleteClientsOrder;
import com.makkras.shop.entity.CompleteStockRefillOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockRefillOrdersJpaRepository extends JpaRepository<CompleteStockRefillOrder,Long> {

    List<CompleteStockRefillOrder> findAllByIsCompleted(boolean completionStatus);
    List<CompleteStockRefillOrder> findAllByOrderByCompleteStockRefillOrderDateDesc();
    List<CompleteStockRefillOrder> findAllByOrderByCompleteStockRefillOrderDateAsc();

    @Modifying
    @Transactional
    @Query(value = "UPDATE complete_stock_refill_orders SET is_completed=? WHERE complete_stock_refill_order_id=?",nativeQuery = true)
    void updateStockRefillOrderStatus(boolean newStatus, Long orderId);

    @Query(value = """
    SELECT DISTINCT * FROM complete_stock_refill_orders
    JOIN users ON complete_stock_refill_orders.user_id=users.user_id
    JOIN supplier_companies ON complete_stock_refill_orders.supplier_company_id = supplier_companies.supplier_company_id
    WHERE (users.login LIKE ? OR users.email LIKE ?) 
    AND supplier_companies.supplier_company_name LIKE ? AND complete_stock_refill_orders.complete_stock_refill_order_date BETWEEN ? AND ?""",nativeQuery = true)
    List<CompleteStockRefillOrder> findAllByUser_LoginLikeOrUser_EmailLikeAndSupplierCompanyNameLikeAndCompleteStockRefillOrderDateIsBetween
            (String login, String email, String supplierCompanyName, LocalDate startDate, LocalDate endDate);
}
