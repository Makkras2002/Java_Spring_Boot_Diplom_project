package com.makkras.shop.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "component_clients_orders")
public class ComponentClientsOrder extends CustomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_clients_order_id")
    private Long componentClientsOrderId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Product.class)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "ordered_product_amount")
    private Long orderedProductAmount;

    @Column(name = "ordered_product_full_price")
    private BigDecimal orderedProductFullPrice;

    public ComponentClientsOrder() {
    }

    public ComponentClientsOrder(Product product, Long orderedProductAmount,
                                 BigDecimal orderedProductFullPrice) {
        this.product = product;
        this.orderedProductAmount = orderedProductAmount;
        this.orderedProductFullPrice = orderedProductFullPrice;
    }

    public ComponentClientsOrder(Long componentClientsOrderId, Product product,
                                 Long orderedProductAmount,
                                 BigDecimal orderedProductFullPrice) {
        this.componentClientsOrderId = componentClientsOrderId;
        this.product = product;
        this.orderedProductAmount = orderedProductAmount;
        this.orderedProductFullPrice = orderedProductFullPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComponentClientsOrder that = (ComponentClientsOrder) o;

        if (componentClientsOrderId != null ? !componentClientsOrderId.equals(that.componentClientsOrderId) : that.componentClientsOrderId != null)
            return false;
        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        if (orderedProductAmount != null ? !orderedProductAmount.equals(that.orderedProductAmount) : that.orderedProductAmount != null)
            return false;
        return orderedProductFullPrice != null ? orderedProductFullPrice.equals(that.orderedProductFullPrice) : that.orderedProductFullPrice == null;
    }

    @Override
    public int hashCode() {
        int result = componentClientsOrderId != null ? componentClientsOrderId.hashCode() : 0;
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (orderedProductAmount != null ? orderedProductAmount.hashCode() : 0);
        result = 31 * result + (orderedProductFullPrice != null ? orderedProductFullPrice.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComponentClientsOrder{");
        sb.append("componentClientsOrderId=").append(componentClientsOrderId);
        sb.append(", product=").append(product);
        sb.append(", orderedProductAmount=").append(orderedProductAmount);
        sb.append(", orderedProductFullPrice=").append(orderedProductFullPrice);
        sb.append('}');
        return sb.toString();
    }
}
