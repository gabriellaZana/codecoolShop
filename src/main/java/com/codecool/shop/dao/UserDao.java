package com.codecool.shop.dao;

import com.codecool.shop.exception.ConnectToStorageFailed;
import com.codecool.shop.model.User;
import java.sql.SQLException;


public interface UserDao {

    void add(User user) throws ConnectToStorageFailed;

    User find(String email) throws ConnectToStorageFailed;

}
