package com.makkras.shop.repo;

import com.makkras.shop.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<ProductCategory,Long> {
    boolean existsByCategoryIdOrCategory(Long id,String category);

    Optional<ProductCategory> findByCategory(String category);
}
