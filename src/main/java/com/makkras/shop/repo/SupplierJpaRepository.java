package com.makkras.shop.repo;

import com.makkras.shop.entity.SupplierCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SupplierJpaRepository extends JpaRepository<SupplierCompany, Long> {

    List<SupplierCompany> findAllByOrderBySupplierCompanyName();
    List<SupplierCompany> findAllByOrderByLocation();
    List<SupplierCompany> findAllByOrderByIsActiveDesc();
    List<SupplierCompany> findAllByOrderByIsActiveAsc();
    List<SupplierCompany> findAllByIsActive(boolean isActive);
    @Transactional
    Long deleteBySupplierCompanyName(String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE supplier_companies SET supplier_company_name=? WHERE supplier_company_id=?", nativeQuery = true)
    void updateSupplierName(String newName,Long supplierId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE supplier_companies SET supplier_company_email=? WHERE supplier_company_id=?", nativeQuery = true)
    void updateSupplierEmail(String newEmail,Long supplierId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE supplier_companies SET tel_number=? WHERE supplier_company_id=?", nativeQuery = true)
    void updateSupplierTelNumber(String newTelNumber,Long supplierId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE supplier_companies SET location=? WHERE supplier_company_id=?", nativeQuery = true)
    void updateSupplierLocation(String newLocation,Long supplierId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE supplier_companies SET is_active=? WHERE supplier_company_id=?", nativeQuery = true)
    void updateSupplierActivityStatus(boolean activityStatus,Long supplierId);
}
