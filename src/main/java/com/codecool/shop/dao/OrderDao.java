package com.codecool.shop.dao;

import com.codecool.shop.model.Order;
import com.codecool.shop.model.ShoppingCart;

import java.sql.SQLException;

public interface OrderDao {

    void add(int userId, ShoppingCart shoppingCart) throws SQLException;
    Order find(int orderId) throws SQLException;

}
