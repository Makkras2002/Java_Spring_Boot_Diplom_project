package com.makkras.shop.repo;

import com.makkras.shop.entity.CompleteClientsOrder;
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
    SELECT DISTINCT * FROM complete_clients_orders 
    JOIN users ON complete_clients_orders.user_id=users.user_id
    WHERE (users.login LIKE ? OR users.email LIKE ?) 
    AND complete_clients_orders.delivery_address LIKE ? AND complete_clients_orders.complete_clients_order_date BETWEEN ? AND ?""",nativeQuery = true)
    List<CompleteClientsOrder> findAllByUser_LoginLikeOrUser_EmailLikeAndDeliveryAddressLikeAndCompleteClientsOrderDateIsBetween
            (String login, String email, String deliveryAddress, LocalDate startDate, LocalDate endDate);
}
