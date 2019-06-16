/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pochesconsulting.dto;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author poche
 */
@Entity
public class OrderDetails {

    @Id
    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "sku_number")
    private String sku;

    @Column(name = "order_amount")
    private Integer amount;

    @Column(name = "total_price")
    private Double totalPrice;
    
    @Column(name="order_status")
    private String orderStatus;
    
    @Column(name = "last_activity")
    private String lastActivity;
    
    public OrderDetails(String orderNumber, String sku, Integer amount, Double totalPrice, String orderStatus, String lastActivity) {
        this.orderNumber = orderNumber;
        this.sku = sku;
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.lastActivity = lastActivity;
    }

    public OrderDetails() {
    }

    @Override
    public String toString() {
        return "OrderDetails{" + "orderNumber=" + orderNumber + ", sku=" + sku + ", amount=" + amount + ", totalPrice=" + totalPrice + ", orderStatus=" + orderStatus + ", lastActivity=" + lastActivity + '}';
    }

  

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

}
