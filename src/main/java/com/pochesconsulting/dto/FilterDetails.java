/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pochesconsulting.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author poche
 */
@Entity
public class FilterDetails {

    @Column(name = "start_range")
    private Double startRange;

    @Column(name = "end_range")
    private Double endRange;

    @Id
    @Column(name = "sku_number")
    private String skuNumber;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "is_pleated")
    private Integer isPleated;

    public FilterDetails(Double start_range, Double end_range, String sku, Double price, Integer pleated) {
        this.startRange = start_range;
        this.endRange = end_range;
        this.skuNumber = sku;
        this.unitPrice = price;
        this.isPleated = pleated;
    }

    public FilterDetails() {

    }

    @Override
    public String toString() {
        return "FilterDetails{" + "start_range=" + startRange + ", end_range=" + endRange + ", sku=" + skuNumber + ", price=" + unitPrice + ", pleated=" + isPleated + '}';
    }

    public Double getStartRange() {
        return startRange;
    }

    public void setStartRange(Double startRange) {
        this.startRange = startRange;
    }

    public Double getEndRange() {
        return endRange;
    }

    public void setEndRange(Double endRange) {
        this.endRange = endRange;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getIsPleated() {
        return isPleated;
    }

    public void setIsPleated(Integer isPleated) {
        this.isPleated = isPleated;
    }
}
