package com.codecool.shop.dao;

import com.codecool.shop.exception.ConnectToStorageFailed;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.ShoppingCart;

import java.sql.SQLException;

public interface OrderDao {

    int add(int userId, ShoppingCart shoppingCart) throws ConnectToStorageFailed;
    Order find(int orderId) throws ConnectToStorageFailed;

}
