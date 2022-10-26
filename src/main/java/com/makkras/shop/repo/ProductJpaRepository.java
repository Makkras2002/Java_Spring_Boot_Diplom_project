package com.makkras.shop.repo;

import com.makkras.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByAmountInStockGreaterThanAndIsAvailable(Long minAmount,boolean availabilityStatus);
}
