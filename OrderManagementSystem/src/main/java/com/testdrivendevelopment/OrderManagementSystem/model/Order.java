package com.testdrivendevelopment.OrderManagementSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     @NotEmpty(message = "Customer name is required")
    private String customerName;
     @PastOrPresent(message = "Order date should be in the past or present")
    private LocalDate orderDate;
     @NotEmpty(message = "Shipping address is required")
    private String shippingAddress;
     @Positive(message = "Total must be a positive value")
    private Double total;
    public Order() { }

    public Order(String customerName, LocalDate orderDate, String shippingAddress, Double total) {
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

}
