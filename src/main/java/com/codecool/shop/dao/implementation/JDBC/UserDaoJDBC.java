package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.User;
import com.codecool.shop.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;


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
        User resultUser = null;
        String findUserQuery = "SELECT * FROM users WHERE email=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(email));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, findUserQuery);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                resultUser = new User(result.getString("email"), result.getString("password"),
                        result.getString("shipping_address"), result.getString("billing_address"),
                        result.getString("firstname"), result.getString("lastname"));
                resultUser.setId(result.getInt("id"));
            }
        }
        return resultUser;
    }
}
