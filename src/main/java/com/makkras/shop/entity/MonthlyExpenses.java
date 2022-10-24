package com.makkras.shop.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "monthly_expenses")
public class MonthlyExpenses extends CustomEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expenses_id")
    private Long expensesId;

    @Column(name = "expenses_name")
    private String expensesName;

    @Column(name = "expenses_amount")
    private BigDecimal expensesAmount;

    public MonthlyExpenses() {
    }

    public MonthlyExpenses(String expensesName, BigDecimal expensesAmount) {
        this.expensesName = expensesName;
        this.expensesAmount = expensesAmount;
    }

    public MonthlyExpenses(Long expensesId, String expensesName,
                           BigDecimal expensesAmount) {
        this.expensesId = expensesId;
        this.expensesName = expensesName;
        this.expensesAmount = expensesAmount;
    }

    public Long getExpensesId() {
        return expensesId;
    }

    public void setExpensesId(Long expensesId) {
        this.expensesId = expensesId;
    }

    public String getExpensesName() {
        return expensesName;
    }

    public void setExpensesName(String expensesName) {
        this.expensesName = expensesName;
    }

    public BigDecimal getExpensesAmount() {
        return expensesAmount;
    }

    public void setExpensesAmount(BigDecimal expensesAmount) {
        this.expensesAmount = expensesAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MonthlyExpenses that = (MonthlyExpenses) o;

        if (expensesId != null ? !expensesId.equals(that.expensesId) : that.expensesId != null) return false;
        if (expensesName != null ? !expensesName.equals(that.expensesName) : that.expensesName != null) return false;
        return expensesAmount != null ? expensesAmount.equals(that.expensesAmount) : that.expensesAmount == null;
    }

    @Override
    public int hashCode() {
        int result = expensesId != null ? expensesId.hashCode() : 0;
        result = 31 * result + (expensesName != null ? expensesName.hashCode() : 0);
        result = 31 * result + (expensesAmount != null ? expensesAmount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MonthlyExpenses{");
        sb.append("expensesId=").append(expensesId);
        sb.append(", expensesName='").append(expensesName).append('\'');
        sb.append(", expensesAmount=").append(expensesAmount);
        sb.append('}');
        return sb.toString();
    }
}
