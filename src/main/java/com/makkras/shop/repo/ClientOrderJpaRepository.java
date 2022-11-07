package com.makkras.shop.repo;

import com.makkras.shop.entity.CompleteClientsOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClientOrderJpaRepository extends JpaRepository<CompleteClientsOrder,Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE complete_clients_orders SET is_completed=? WHERE complete_clients_order_id=?",nativeQuery = true)
    void updateClientOrderStatus(boolean newStatus, Long orderId);
    List<CompleteClientsOrder> findAllByIsCompleted(boolean completionStatus);
}
