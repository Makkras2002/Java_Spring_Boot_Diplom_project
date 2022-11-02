package com.makkras.shop.repo;

import com.makkras.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByAmountInStockGreaterThanAndIsAvailable(Long minAmount, boolean availabilityStatus);
    List<Product> findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductPriceAsc(Long minAmount,boolean availabilityStatus);
    List<Product> findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductPriceDesc(Long minAmount,boolean availabilityStatus);
    List<Product> findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductName(Long minAmount,boolean availabilityStatus);
    List<Product> findAllByAmountInStockGreaterThanAndIsAvailableOrderByCategoryCategory(Long minAmount,boolean availabilityStatus);
}
