package com.bookstore.model;

import java.util.Date;

public class Orders {

    private int orderId;
    private Date orderDate;
    private double totalAmount;
    private String username;
    private int userId;
    private String phone;
    private String status;
    private String address;

    public Orders() {
    }

    public Orders(int orderId, Date orderDate, double totalAmount,
                 int userId, String status, String address) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.userId = userId;
        this.status = status;
        this.address = address;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}