package com.devsebastian.gtbit;

import java.io.Serializable;
import java.util.ArrayList;

public class Bill implements Serializable {
    String shopName;
    String id;
    ArrayList<BillItem> items;

    public Bill() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bill(String shopName, ArrayList<BillItem> items) {
        this.shopName = shopName;
        this.items = items;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public ArrayList<BillItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<BillItem> items) {
        this.items = items;
    }
}
