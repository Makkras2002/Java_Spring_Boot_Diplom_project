package com.makkras.shop.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "component_stock_refill_orders")
public class ComponentStockRefillOrder extends CustomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_stock_refill_order_id")
    private Long componentStockRefillOrderId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Product.class)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "ordered_product_amount")
    private Long orderedProductAmount;

    @Column(name = "ordered_product_full_price")
    private BigDecimal orderedProductFullPrice;

    public ComponentStockRefillOrder() {
    }

    public ComponentStockRefillOrder(Product product, Long orderedProductAmount,
                                     BigDecimal orderedProductFullPrice) {
        this.product = product;
        this.orderedProductAmount = orderedProductAmount;
        this.orderedProductFullPrice = orderedProductFullPrice;
    }

    public ComponentStockRefillOrder(Long componentStockRefillOrderId, Product product,
                                     Long orderedProductAmount,
                                     BigDecimal orderedProductFullPrice) {
        this.componentStockRefillOrderId = componentStockRefillOrderId;
        this.product = product;
        this.orderedProductAmount = orderedProductAmount;
        this.orderedProductFullPrice = orderedProductFullPrice;
    }

    public Long getComponentStockRefillOrderId() {
        return componentStockRefillOrderId;
    }

    public void setComponentStockRefillOrderId(Long componentStockRefillOrderId) {
        this.componentStockRefillOrderId = componentStockRefillOrderId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getOrderedProductAmount() {
        return orderedProductAmount;
    }

    public void setOrderedProductAmount(Long orderedProductAmount) {
        this.orderedProductAmount = orderedProductAmount;
    }

    public BigDecimal getOrderedProductFullPrice() {
        return orderedProductFullPrice;
    }

    public void setOrderedProductFullPrice(BigDecimal orderedProductFullPrice) {
        this.orderedProductFullPrice = orderedProductFullPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComponentStockRefillOrder that = (ComponentStockRefillOrder) o;

        if (componentStockRefillOrderId != null ? !componentStockRefillOrderId.equals(that.componentStockRefillOrderId) : that.componentStockRefillOrderId != null)
            return false;
        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        if (orderedProductAmount != null ? !orderedProductAmount.equals(that.orderedProductAmount) : that.orderedProductAmount != null)
            return false;
        return orderedProductFullPrice != null ? orderedProductFullPrice.equals(that.orderedProductFullPrice) : that.orderedProductFullPrice == null;
    }

    @Override
    public int hashCode() {
        int result = componentStockRefillOrderId != null ? componentStockRefillOrderId.hashCode() : 0;
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (orderedProductAmount != null ? orderedProductAmount.hashCode() : 0);
        result = 31 * result + (orderedProductFullPrice != null ? orderedProductFullPrice.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComponentStockRefillOrder{");
        sb.append("componentStockRefillOrderId=").append(componentStockRefillOrderId);
        sb.append(", product=").append(product);
        sb.append(", orderedProductAmount=").append(orderedProductAmount);
        sb.append(", orderedProductFullPrice=").append(orderedProductFullPrice);
        sb.append('}');
        return sb.toString();
    }
}
