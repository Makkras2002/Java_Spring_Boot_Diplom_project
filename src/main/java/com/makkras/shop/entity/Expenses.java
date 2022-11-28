package com.makkras.shop.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "expenses")
public class Expenses extends CustomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expenses_id")
    private Long expensesId;

    @Column(name = "expenses_name")
    private String expensesName;

    @Column(name = "expenses_amount")
    private BigDecimal expensesAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "expenses_period")
    private PeriodType period;

    public Expenses() {
    }

    public Expenses(String expensesName, BigDecimal expensesAmount, PeriodType period) {
        this.expensesName = expensesName;
        this.expensesAmount = expensesAmount;
        this.period = period;
    }

    public Expenses(Long expensesId, String expensesName, BigDecimal expensesAmount, PeriodType period) {
        this.expensesId = expensesId;
        this.expensesName = expensesName;
        this.expensesAmount = expensesAmount;
        this.period = period;
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

    public PeriodType getPeriod() {
        return period;
    }

    public void setPeriod(PeriodType period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expenses expenses = (Expenses) o;

        if (expensesId != null ? !expensesId.equals(expenses.expensesId) : expenses.expensesId != null) return false;
        if (expensesName != null ? !expensesName.equals(expenses.expensesName) : expenses.expensesName != null)
            return false;
        if (expensesAmount != null ? !expensesAmount.equals(expenses.expensesAmount) : expenses.expensesAmount != null)
            return false;
        return period == expenses.period;
    }

    @Override
    public int hashCode() {
        int result = expensesId != null ? expensesId.hashCode() : 0;
        result = 31 * result + (expensesName != null ? expensesName.hashCode() : 0);
        result = 31 * result + (expensesAmount != null ? expensesAmount.hashCode() : 0);
        result = 31 * result + (period != null ? period.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Expenses{");
        sb.append("expensesId=").append(expensesId);
        sb.append(", expensesName='").append(expensesName).append('\'');
        sb.append(", expensesAmount=").append(expensesAmount);
        sb.append(", period=").append(period);
        sb.append('}');
        return sb.toString();
    }
}
