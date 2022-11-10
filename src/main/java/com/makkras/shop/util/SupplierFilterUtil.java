package com.makkras.shop.util;

import com.makkras.shop.entity.SupplierCompany;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierFilterUtil {
    public List<SupplierCompany> filterSuppliers(List<SupplierCompany> allSuppliers,
                                                 String supplierName,
                                                 String supplierEmail,
                                                 String supplierTelNumber,
                                                 String supplierLocation) {
        return allSuppliers.stream()
                .filter(supplierCompany -> supplierName==null ||
                        (supplierCompany.getSupplierCompanyName().toLowerCase().contains(supplierName.toLowerCase()) && supplierName.length()>2) ||
                        supplierName.length()==0)
                .filter(supplierCompany -> supplierEmail==null ||
                        (supplierCompany.getSupplierCompanyEmail().toLowerCase().contains(supplierEmail.toLowerCase()) && supplierEmail.length()>2) ||
                        supplierEmail.length()==0)
                .filter(supplierCompany -> supplierTelNumber==null ||
                        (supplierCompany.getTelNumber().toLowerCase().contains(supplierTelNumber.toLowerCase()) && supplierTelNumber.length()>2) ||
                        supplierTelNumber.length()==0)
                .filter(supplierCompany -> supplierLocation==null ||
                        (supplierCompany.getLocation().toLowerCase().contains(supplierLocation.toLowerCase()) && supplierLocation.length()>2) ||
                        supplierLocation.length()==0)
                .collect(Collectors.toList());
    }
}
