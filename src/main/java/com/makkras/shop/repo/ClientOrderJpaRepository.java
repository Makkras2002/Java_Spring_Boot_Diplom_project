package com.makkras.shop.repo;

import com.makkras.shop.entity.CompleteClientsOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientOrderJpaRepository extends JpaRepository<CompleteClientsOrder,Long> {

}
