package com.makkras.shop.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "complete_stock_refill_orders")
public class CompleteStockRefillOrder extends CustomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complete_stock_refill_order_id")
    private Long completeStockRefillOrderId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Column(name = "complete_stock_refill_order_date")
    private LocalDate completeStockRefillOrderDate;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = SupplierCompany.class)
    @JoinColumn(name = "supplier_company_id")
    private SupplierCompany supplierCompany;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "complete_stock_refill_order_id")
    private List<ComponentStockRefillOrder> stockRefillComponentOrders;

    public CompleteStockRefillOrder() {
    }

    public CompleteStockRefillOrder(User user, boolean isCompleted,
                                    LocalDate completeStockRefillOrderDate,
                                    SupplierCompany supplierCompany,
                                    List<ComponentStockRefillOrder> stockRefillComponentOrders) {
        this.user = user;
        this.isCompleted = isCompleted;
        this.completeStockRefillOrderDate = completeStockRefillOrderDate;
        this.supplierCompany = supplierCompany;
        this.stockRefillComponentOrders = stockRefillComponentOrders;
    }

    public CompleteStockRefillOrder(Long completeStockRefillOrderId, User user,
                                    boolean isCompleted,
                                    LocalDate completeStockRefillOrderDate,
                                    SupplierCompany supplierCompany,
                                    List<ComponentStockRefillOrder> stockRefillComponentOrders) {
        this.completeStockRefillOrderId = completeStockRefillOrderId;
        this.user = user;
        this.isCompleted = isCompleted;
        this.completeStockRefillOrderDate = completeStockRefillOrderDate;
        this.supplierCompany = supplierCompany;
        this.stockRefillComponentOrders = stockRefillComponentOrders;
    }

    public Long getCompleteStockRefillOrderId() {
        return completeStockRefillOrderId;
    }

    public void setCompleteStockRefillOrderId(Long completeStockRefillOrderId) {
        this.completeStockRefillOrderId = completeStockRefillOrderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public LocalDate getCompleteStockRefillOrderDate() {
        return completeStockRefillOrderDate;
    }

    public void setCompleteStockRefillOrderDate(LocalDate completeStockRefillOrderDate) {
        this.completeStockRefillOrderDate = completeStockRefillOrderDate;
    }

    public SupplierCompany getSupplierCompany() {
        return supplierCompany;
    }

    public void setSupplierCompany(SupplierCompany supplierCompany) {
        this.supplierCompany = supplierCompany;
    }

    public List<ComponentStockRefillOrder> getStockRefillComponentOrders() {
        return stockRefillComponentOrders;
    }

    public void setStockRefillComponentOrders(List<ComponentStockRefillOrder> stockRefillComponentOrders) {
        this.stockRefillComponentOrders = stockRefillComponentOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompleteStockRefillOrder that = (CompleteStockRefillOrder) o;

        if (isCompleted != that.isCompleted) return false;
        if (completeStockRefillOrderId != null ? !completeStockRefillOrderId.equals(that.completeStockRefillOrderId) : that.completeStockRefillOrderId != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (completeStockRefillOrderDate != null ? !completeStockRefillOrderDate.equals(that.completeStockRefillOrderDate) : that.completeStockRefillOrderDate != null)
            return false;
        if (supplierCompany != null ? !supplierCompany.equals(that.supplierCompany) : that.supplierCompany != null)
            return false;
        return stockRefillComponentOrders != null ? stockRefillComponentOrders.equals(that.stockRefillComponentOrders) : that.stockRefillComponentOrders == null;
    }

    @Override
    public int hashCode() {
        int result = completeStockRefillOrderId != null ? completeStockRefillOrderId.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (isCompleted ? 1 : 0);
        result = 31 * result + (completeStockRefillOrderDate != null ? completeStockRefillOrderDate.hashCode() : 0);
        result = 31 * result + (supplierCompany != null ? supplierCompany.hashCode() : 0);
        result = 31 * result + (stockRefillComponentOrders != null ? stockRefillComponentOrders.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CompleteStockRefillOrder{");
        sb.append("completeStockRefillOrderId=").append(completeStockRefillOrderId);
        sb.append(", user=").append(user);
        sb.append(", isCompleted=").append(isCompleted);
        sb.append(", completeStockRefillOrderDate=").append(completeStockRefillOrderDate);
        sb.append(", supplierCompany=").append(supplierCompany);
        sb.append(", stockRefillComponentOrders=").append(stockRefillComponentOrders);
        sb.append('}');
        return sb.toString();
    }
}
