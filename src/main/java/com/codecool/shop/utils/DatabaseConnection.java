package com.codecool.shop.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instance = null;

    // INFOs from Properties
    private static Properties properties = new Properties();

    // Connection object
    private static Connection connection = null;

    //  Database credentials
    private static String host;
    private static String databaseName;
    private static String DB_URL;
    private static String JDBC_DRIVER = "org.postgresql.Driver";
    private static String USER;
    private static String PASSWORD;
    private String pass= "src/main/resources/sql/connection.properties";


    private DatabaseConnection(String filePath) {
        // load a properties file
        try {
            InputStream input = new FileInputStream(filePath);
            properties.load(input);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setup();
    }

    public static DatabaseConnection getInstance(String filePath) {
        if (instance == null) {
            instance = new DatabaseConnection(filePath);
        }
        return instance;
    }

    
    private static void setup() {
        host = properties.getProperty("url");
        databaseName = properties.getProperty("database");
        DB_URL = "jdbc:postgresql://" + host + "/" + databaseName;
        USER = properties.getProperty("user");
        PASSWORD = properties.getProperty("password");
    }


    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");

            //STEP 3: Open a connection
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return connection;
    }
}
