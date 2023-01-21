package com.makkras.shop.repo;

import com.makkras.shop.entity.CompleteClientsOrder;
import com.makkras.shop.repo.projection_interface.MoneyByDateStatistics;
import com.makkras.shop.repo.projection_interface.ProductsSellingStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientOrderJpaRepository extends JpaRepository<CompleteClientsOrder,Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE complete_clients_orders SET is_completed=? WHERE complete_clients_order_id=?",nativeQuery = true)
    void updateClientOrderStatus(boolean newStatus, Long orderId);
    List<CompleteClientsOrder> findAllByIsCompleted(boolean completionStatus);
    List<CompleteClientsOrder> findAllByOrderByCompleteClientsOrderDateDesc();
    List<CompleteClientsOrder> findAllByOrderByCompleteClientsOrderDateAsc();

    @Query(value = """
    SELECT product_name AS name,SUM(ordered_product_amount) AS amount FROM complete_clients_orders 
    JOIN component_clients_orders ON complete_clients_orders.complete_clients_order_id = component_clients_orders.complete_clients_order_id
    JOIN products ON component_clients_orders.product_id = products.product_id WHERE complete_clients_orders.is_completed = true AND complete_clients_orders.complete_clients_order_date BETWEEN ? AND ? GROUP BY product_name""",nativeQuery = true)
    List<ProductsSellingStatistics> countProductsSellingStatistics(LocalDate startDate, LocalDate endDate);

    @Query(value = """
    SELECT complete_clients_order_date AS date,SUM(component_clients_orders.ordered_product_full_price) AS amount FROM complete_clients_orders 
    JOIN component_clients_orders ON complete_clients_orders.complete_clients_order_id = component_clients_orders.complete_clients_order_id
    WHERE complete_clients_orders.is_completed = true AND complete_clients_orders.complete_clients_order_date BETWEEN ? AND ? GROUP BY complete_clients_order_date ORDER BY complete_clients_order_date ASC""",nativeQuery = true)
    List<MoneyByDateStatistics> countEarningsByDateStatisticsAndOrderByCompleteClientsOrderDateAsc(LocalDate startDate, LocalDate endDate);

    @Query(value = """
    SELECT DISTINCT * FROM complete_clients_orders 
    JOIN users ON complete_clients_orders.user_id=users.user_id
    WHERE (users.login LIKE ? OR users.email LIKE ?) 
    AND complete_clients_orders.delivery_address LIKE ? AND complete_clients_orders.complete_clients_order_date BETWEEN ? AND ?""",nativeQuery = true)
    List<CompleteClientsOrder> findAllByUser_LoginLikeOrUser_EmailLikeAndDeliveryAddressLikeAndCompleteClientsOrderDateIsBetween
            (String login, String email, String deliveryAddress, LocalDate startDate, LocalDate endDate);
}
