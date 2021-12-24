package com.example.kms;

import java.io.Serializable;

public class OrderProduct implements Serializable {

    String ProductName;
    int quantity;


    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
