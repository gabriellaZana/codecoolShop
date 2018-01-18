package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.exception.ConnectToStorageFailed;
import com.codecool.shop.model.User;
import com.codecool.shop.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
    public void add(User user) throws ConnectToStorageFailed {

        String addQuery = "INSERT INTO users (email, password) VALUES (?, ?);";

        ArrayList<Object> infos = new ArrayList<>(Arrays.asList(user.getEmail(), user.getPassword()));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, addQuery)) {
            logger.info("Connected to database");
            statement.executeUpdate();
        }catch (SQLException e){
            throw new ConnectToStorageFailed(e.getMessage());
        }

    }

    
    @Override
    public User find(String email) throws ConnectToStorageFailed {

        String query = "SELECT * FROM users WHERE email =?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(email));
        if(executeFindQuery(query, infos) == null){
            return null;
        }
        return executeFindQuery(query, infos);

    }

    private User executeFindQuery(String query, ArrayList<Object> infos) throws ConnectToStorageFailed {
        User resultUser = null;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                resultUser = new User(result.getString("email"));
                resultUser.setPassword(result.getString("password"));
                //resultUser.setId("id");
                resultUser.setId(result.getString("id"));
            }
        }
        catch (SQLException e){
            throw new ConnectToStorageFailed(e.getMessage());
        }
        return resultUser;
    }
}
