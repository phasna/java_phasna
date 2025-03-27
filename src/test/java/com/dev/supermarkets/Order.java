package com.dev.supermarkets;

import java.time.LocalDate;

public class Order {
    private int orderId;
    private LocalDate orderDate;
    private int productId;
    private int quantity;

    public Order(int orderId, LocalDate orderDate, int productId, int quantity) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters
    public int getOrderId() { return orderId; }
    public LocalDate getOrderDate() { return orderDate; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}