package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.User;
import com.codecool.shop.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserDaoJDBC implements UserDao{
    private final static Logger logger = LoggerFactory.getLogger(UserDaoJDBC.class);

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

        String addQuery = "INSERT INTO users (email, password) VALUES (?, ?);";

        ArrayList<Object> infos = new ArrayList<>(Arrays.asList(user.getEmail(), user.getPassword()));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, addQuery)) {
            logger.info("Connected to database");
            statement.executeUpdate();
            logger.info("Query done");
        }

    }

    
    @Override
    public User find(String email) throws SQLException {
        return null;
    }
}
