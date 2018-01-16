package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.User;
import com.codecool.shop.utils.DatabaseConnection;

import java.sql.SQLException;


public class UserDaoJDBC implements UserDao{
    private static UserDaoJDBC instance = null;
    private String filePath = "src/main/resources/sql/connection.properties";
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);

    private UserDaoJDBC() {
    }

    public static UserDaoJDBC getInstance() {
        if (instance == null) {
            instance = new UserDaoJDBC();
        }
        return instance;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public void add(User user) throws SQLException {

    }

    
    @Override
    public User find(String email) throws SQLException {
        return null;
    }
}
