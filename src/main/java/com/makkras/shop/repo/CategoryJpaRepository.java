package com.makkras.shop.repo;

import com.makkras.shop.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpaRepository extends JpaRepository<ProductCategory,Long> {

    boolean existsByCategoryIdAndCategory(Long id,String category);
}
