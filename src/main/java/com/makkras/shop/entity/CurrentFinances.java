package com.makkras.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity(name = "current_finances")
public class CurrentFinances extends CustomEntity {
    @Id
    @Column(name = "finances_id")
    private Long financesId;

    @Column(name = "finances_amount")
    private BigDecimal financesAmount;

    public CurrentFinances() {
    }

    public CurrentFinances(BigDecimal financesAmount) {
        this.financesAmount = financesAmount;
    }

    public CurrentFinances(Long financesId, BigDecimal financesAmount) {
        this.financesId = financesId;
        this.financesAmount = financesAmount;
    }

    public Long getFinancesId() {
        return financesId;
    }

    public void setFinancesId(Long financesId) {
        this.financesId = financesId;
    }

    public BigDecimal getFinancesAmount() {
        return financesAmount;
    }

    public void setFinancesAmount(BigDecimal financesAmount) {
        this.financesAmount = financesAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrentFinances that = (CurrentFinances) o;

        if (financesId != null ? !financesId.equals(that.financesId) : that.financesId != null) return false;
        return financesAmount != null ? financesAmount.equals(that.financesAmount) : that.financesAmount == null;
    }

    @Override
    public int hashCode() {
        int result = financesId != null ? financesId.hashCode() : 0;
        result = 31 * result + (financesAmount != null ? financesAmount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CurrentFinances{");
        sb.append("financesId=").append(financesId);
        sb.append(", financesAmount=").append(financesAmount);
        sb.append('}');
        return sb.toString();
    }
}
