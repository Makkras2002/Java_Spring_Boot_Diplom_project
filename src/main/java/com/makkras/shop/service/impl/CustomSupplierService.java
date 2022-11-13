package com.makkras.shop.service.impl;

import com.makkras.shop.entity.SupplierCompany;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.repo.SupplierJpaRepository;
import com.makkras.shop.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomSupplierService implements SupplierService {
    private final SupplierJpaRepository supplierJpaRepository;

    @Autowired
    public CustomSupplierService(SupplierJpaRepository supplierJpaRepository) {
        this.supplierJpaRepository = supplierJpaRepository;
    }

    @Override
    public List<SupplierCompany> getAllSupplierCompanies() {
        return supplierJpaRepository.findAll();
    }

    @Override
    public void addSupplier(SupplierCompany supplierCompany) {
        supplierJpaRepository.save(supplierCompany);
    }

    @Override
    public boolean updateSupplierData(SupplierCompany updatedSupplier) {
        Long supplierForUpdateId = updatedSupplier.getSupplierCompanyId();
        Optional<SupplierCompany> oldSupplierCompanyOptional = supplierJpaRepository.findById(supplierForUpdateId);
        if(oldSupplierCompanyOptional.isPresent()) {
            SupplierCompany oldSupplier = oldSupplierCompanyOptional.get();
            if(!updatedSupplier.getSupplierCompanyName().equals(oldSupplier.getSupplierCompanyName())) {
                supplierJpaRepository.updateSupplierName(updatedSupplier.getSupplierCompanyName(),supplierForUpdateId);
            }
            if(!updatedSupplier.getSupplierCompanyEmail().equals(oldSupplier.getSupplierCompanyEmail())) {
                supplierJpaRepository.updateSupplierEmail(updatedSupplier.getSupplierCompanyEmail(),supplierForUpdateId);
            }
            if(!updatedSupplier.getTelNumber().equals(oldSupplier.getTelNumber())) {
                supplierJpaRepository.updateSupplierTelNumber(updatedSupplier.getTelNumber(),supplierForUpdateId);
            }
            if(!updatedSupplier.getLocation().equals(oldSupplier.getLocation())) {
                supplierJpaRepository.updateSupplierLocation(updatedSupplier.getLocation(),supplierForUpdateId);
            }
            if(updatedSupplier.isActive() != oldSupplier.isActive()) {
                supplierJpaRepository.updateSupplierActivityStatus(updatedSupplier.isActive(),supplierForUpdateId);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<SupplierCompany> getAllSupplierCompaniesAndOrderByName() {
        return supplierJpaRepository.findAllByOrderBySupplierCompanyName();
    }

    @Override
    public List<SupplierCompany> getAllSupplierCompaniesAndOrderByLocation() {
        return supplierJpaRepository.findAllByOrderByLocation();
    }

    @Override
    public List<SupplierCompany> getAllSupplierCompaniesAndOrderByIsActiveDesc() {
        return supplierJpaRepository.findAllByOrderByIsActiveDesc();
    }

    @Override
    public List<SupplierCompany> getAllSupplierCompaniesAndOrderByIsActiveAsc() {
        return supplierJpaRepository.findAllByOrderByIsActiveAsc();
    }

    @Override
    public SupplierCompany getSupplierCompanyById(Long supplierCompanyId) throws CustomServiceException {
        return supplierJpaRepository.findById(supplierCompanyId).orElseThrow(CustomServiceException::new);
    }

    @Override
    public List<SupplierCompany> getAllSupplierCompaniesWithActivityStatus(boolean isActive) {
        return supplierJpaRepository.findAllByIsActive(isActive);
    }
}
