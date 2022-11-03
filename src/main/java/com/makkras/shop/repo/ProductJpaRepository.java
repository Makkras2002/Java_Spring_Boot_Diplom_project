package com.makkras.shop.repo;

import com.makkras.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByAmountInStockGreaterThanAndIsAvailable(Long minAmount, boolean availabilityStatus);
    List<Product> findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductPriceAsc(Long minAmount,boolean availabilityStatus);
    List<Product> findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductPriceDesc(Long minAmount,boolean availabilityStatus);
    List<Product> findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductName(Long minAmount,boolean availabilityStatus);
    List<Product> findAllByAmountInStockGreaterThanAndIsAvailableOrderByCategoryCategory(Long minAmount,boolean availabilityStatus);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Products SET product_name=? WHERE product_id=?", nativeQuery = true)
    void updateProductName(String newName,Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Products SET product_price=? WHERE product_id=?", nativeQuery = true)
    void updateProductPrice(BigDecimal newPrice, Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Products SET amount_in_stock=? WHERE product_id=?", nativeQuery = true)
    void updateProductAmountInStock(Long amountInStock, Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Products SET picture_path=? WHERE product_id=?", nativeQuery = true)
    void updateProductPicturePath(String picturePath, Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Products SET product_comment=? WHERE product_id=?", nativeQuery = true)
    void updateProductComment(String comment, Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Products SET is_available=? WHERE product_id=?", nativeQuery = true)
    void updateProductAvailability(boolean isAvailable, Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Products SET product_category_id=? WHERE product_id=?", nativeQuery = true)
    void updateProductCategory(Long productCategoryId, Long productId);
}
