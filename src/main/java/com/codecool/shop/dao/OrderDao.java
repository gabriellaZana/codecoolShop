package com.codecool.shop.dao;

import com.codecool.shop.model.Order;
import com.codecool.shop.model.ShoppingCart;

public interface OrderDao {

    void add(int userId, ShoppingCart shoppingCart);
    Order find(int orderId);
    
}
