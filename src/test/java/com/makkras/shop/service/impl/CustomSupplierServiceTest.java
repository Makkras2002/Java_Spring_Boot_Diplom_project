package com.makkras.shop.service.impl;

import com.makkras.shop.entity.SupplierCompany;
import com.makkras.shop.service.SupplierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomSupplierServiceTest {
    @Autowired
    private SupplierService supplierService;
    private SupplierCompany supplierCompany;
    private static final String SUPPLIER_COMPANY_NAME = "TEST_COMPANY";
    private static final String SUPPLIER_COMPANY_EMAIL = "TEST_EMAIL@gmail.com";
    private static final String SUPPLIER_COMPANY_TEL_NUMBER = "+375440000000";
    private static final String SUPPLIER_COMPANY_LOCATION = "TEST_LOCATION";
    private static final boolean SUPPLIER_COMPANY_STATUS = true;

    @Before()
    public void initializeData() {
        supplierCompany = new SupplierCompany(
                SUPPLIER_COMPANY_NAME,
                SUPPLIER_COMPANY_EMAIL,
                SUPPLIER_COMPANY_TEL_NUMBER,
                SUPPLIER_COMPANY_LOCATION,
                SUPPLIER_COMPANY_STATUS);
    }

    @Test()
    public void testAddSupplier() {
        SupplierCompany savedSupplierCompany = supplierService.addSupplierAndReturn(supplierCompany);
        assertNotEquals(savedSupplierCompany,null);
    }

    @Test()
    public void testDeleteSupplierData() {
        Long deletedId = supplierService.deleteSupplierCompanyByNameAndReturnDeletedId(SUPPLIER_COMPANY_NAME);
        assertNotEquals(deletedId,null);
    }
}