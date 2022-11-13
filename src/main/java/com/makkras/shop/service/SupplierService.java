package com.makkras.shop.service;

import com.makkras.shop.entity.SupplierCompany;
import com.makkras.shop.exception.CustomServiceException;

import java.util.List;

public interface SupplierService {
    List<SupplierCompany> getAllSupplierCompanies();
    void addSupplier(SupplierCompany supplierCompany);
    boolean updateSupplierData(SupplierCompany updatedSupplier);
    List<SupplierCompany> getAllSupplierCompaniesAndOrderByName();
    List<SupplierCompany> getAllSupplierCompaniesAndOrderByLocation();
    List<SupplierCompany> getAllSupplierCompaniesAndOrderByIsActiveDesc();
    List<SupplierCompany> getAllSupplierCompaniesAndOrderByIsActiveAsc();
    SupplierCompany getSupplierCompanyById(Long supplierCompanyId) throws CustomServiceException;
    List<SupplierCompany> getAllSupplierCompaniesWithActivityStatus(boolean isActive);
}
