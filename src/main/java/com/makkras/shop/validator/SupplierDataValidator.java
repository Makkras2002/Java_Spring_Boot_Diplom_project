package com.makkras.shop.validator;

import com.makkras.shop.entity.SupplierCompany;

import java.util.List;

public interface SupplierDataValidator {
    boolean validateSupplierData(SupplierCompany company, List<String> invalidParams);
}
