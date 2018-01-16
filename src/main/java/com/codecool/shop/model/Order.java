package com.codecool.shop.model;

import java.util.List;

public class Order {

    private int userId;
    private List<Product> productList;

    public Order(int userId, List<Product> productList) {
        this.userId = userId;
        this.productList = productList;
    }

    public int getUserId() {
        return userId;
    }

    public List<Product> getProductList() {
        return productList;
    }
}
