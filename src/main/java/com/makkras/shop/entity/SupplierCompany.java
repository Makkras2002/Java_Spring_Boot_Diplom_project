package com.makkras.shop.entity;

import javax.persistence.*;

@Entity(name = "supplier_companies")
public class SupplierCompany extends CustomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_company_id")
    private Long supplierCompanyId;

    @Column(name = "supplier_company_name")
    private String supplierCompanyName;

    @Column(name = "supplier_company_email")
    private String supplierCompanyEmail;

    @Column(name = "is_active")
    private boolean isActive;
    public SupplierCompany() {
    }

    public SupplierCompany(String supplierCompanyName, String supplierCompanyEmail, boolean isActive) {
        this.supplierCompanyName = supplierCompanyName;
        this.supplierCompanyEmail = supplierCompanyEmail;
        this.isActive = isActive;
    }

    public SupplierCompany(Long supplierCompanyId, String supplierCompanyName, String supplierCompanyEmail, boolean isActive) {
        this.supplierCompanyId = supplierCompanyId;
        this.supplierCompanyName = supplierCompanyName;
        this.supplierCompanyEmail = supplierCompanyEmail;
        this.isActive = isActive;
    }

    public Long getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Long supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public String getSupplierCompanyEmail() {
        return supplierCompanyEmail;
    }

    public void setSupplierCompanyEmail(String supplierCompanyEmail) {
        this.supplierCompanyEmail = supplierCompanyEmail;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupplierCompany that = (SupplierCompany) o;

        if (isActive != that.isActive) return false;
        if (supplierCompanyId != null ? !supplierCompanyId.equals(that.supplierCompanyId) : that.supplierCompanyId != null)
            return false;
        if (supplierCompanyName != null ? !supplierCompanyName.equals(that.supplierCompanyName) : that.supplierCompanyName != null)
            return false;
        return supplierCompanyEmail != null ? supplierCompanyEmail.equals(that.supplierCompanyEmail) : that.supplierCompanyEmail == null;
    }

    @Override
    public int hashCode() {
        int result = supplierCompanyId != null ? supplierCompanyId.hashCode() : 0;
        result = 31 * result + (supplierCompanyName != null ? supplierCompanyName.hashCode() : 0);
        result = 31 * result + (supplierCompanyEmail != null ? supplierCompanyEmail.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SupplierCompany{");
        sb.append("supplierCompanyId=").append(supplierCompanyId);
        sb.append(", supplierCompanyName='").append(supplierCompanyName).append('\'');
        sb.append(", supplierCompanyEmail='").append(supplierCompanyEmail).append('\'');
        sb.append(", isActive=").append(isActive);
        sb.append('}');
        return sb.toString();
    }
}
