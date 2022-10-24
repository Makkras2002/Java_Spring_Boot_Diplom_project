package com.makkras.shop.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "complete_clients_orders")
public class CompleteClientsOrder extends CustomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complete_clients_order_id")
    private Long completeClientsOrderId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Column(name = "complete_clients_order_date")
    private LocalDate completeClientsOrderDate;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "complete_clients_order_id")
    private List<ComponentClientsOrder> clientsComponentOrders;

    public CompleteClientsOrder() {
    }

    public CompleteClientsOrder(User user, boolean isCompleted,
                                LocalDate completeClientsOrderDate,
                                List<ComponentClientsOrder> clientsComponentOrders) {
        this.user = user;
        this.isCompleted = isCompleted;
        this.completeClientsOrderDate = completeClientsOrderDate;
        this.clientsComponentOrders = clientsComponentOrders;
    }

    public CompleteClientsOrder(Long completeClientsOrderId, User user,
                                boolean isCompleted, LocalDate completeClientsOrderDate,
                                List<ComponentClientsOrder> clientsComponentOrders) {
        this.completeClientsOrderId = completeClientsOrderId;
        this.user = user;
        this.isCompleted = isCompleted;
        this.completeClientsOrderDate = completeClientsOrderDate;
        this.clientsComponentOrders = clientsComponentOrders;
    }

    public Long getCompleteClientsOrderId() {
        return completeClientsOrderId;
    }

    public void setCompleteClientsOrderId(Long completeClientsOrderId) {
        this.completeClientsOrderId = completeClientsOrderId;
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

    public LocalDate getCompleteClientsOrderDate() {
        return completeClientsOrderDate;
    }

    public void setCompleteClientsOrderDate(LocalDate completeClientsOrderDate) {
        this.completeClientsOrderDate = completeClientsOrderDate;
    }

    public List<ComponentClientsOrder> getClientsComponentOrders() {
        return clientsComponentOrders;
    }

    public void setClientsComponentOrders(List<ComponentClientsOrder> clientsComponentOrders) {
        this.clientsComponentOrders = clientsComponentOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompleteClientsOrder that = (CompleteClientsOrder) o;

        if (isCompleted != that.isCompleted) return false;
        if (completeClientsOrderId != null ? !completeClientsOrderId.equals(that.completeClientsOrderId) : that.completeClientsOrderId != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (completeClientsOrderDate != null ? !completeClientsOrderDate.equals(that.completeClientsOrderDate) : that.completeClientsOrderDate != null)
            return false;
        return clientsComponentOrders != null ? clientsComponentOrders.equals(that.clientsComponentOrders) : that.clientsComponentOrders == null;
    }

    @Override
    public int hashCode() {
        int result = completeClientsOrderId != null ? completeClientsOrderId.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (isCompleted ? 1 : 0);
        result = 31 * result + (completeClientsOrderDate != null ? completeClientsOrderDate.hashCode() : 0);
        result = 31 * result + (clientsComponentOrders != null ? clientsComponentOrders.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CompleteClientsOrder{");
        sb.append("completeClientsOrderId=").append(completeClientsOrderId);
        sb.append(", user=").append(user);
        sb.append(", isCompleted=").append(isCompleted);
        sb.append(", completeClientsOrderDate=").append(completeClientsOrderDate);
        sb.append(", clientsComponentOrders=").append(clientsComponentOrders);
        sb.append('}');
        return sb.toString();
    }
}
