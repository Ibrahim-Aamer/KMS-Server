package com.example.kms;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
   private  Double Total;
   private int TableNo;
   private ArrayList<OrderProduct> order_products;
   Order(Double t, int tab, ArrayList<OrderProduct> orders)
   {
       Total=t;
       TableNo=tab;
       order_products=orders;
   }
    public void setTotal(Double total) {
        Total = total;
    }

    public void setTbaleNo(int tbaleNo) {
        TableNo = tbaleNo;
    }

    public void setOrder_products(ArrayList<OrderProduct> order_products) {
        this.order_products = order_products;
    }

    public Double getTotal() {
        return Total;
    }

    public int getTbaleNo() {
        return TableNo;
    }

    public ArrayList<OrderProduct> getOrder_products() {
        return order_products;
    }


}
