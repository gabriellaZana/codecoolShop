package com.codecool.shop.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.codecool.shop.exception.ConnectToStorageFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database connection class, which takes care about the connection to the database.
 */
public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

    private static DatabaseConnection instance = null;

    // INFOs from Properties
    private static Properties properties = new Properties();

    //  Database credentials
    private static String host;
    private static String databaseName;
    private static String DB_URL;
    private static String JDBC_DRIVER = "org.postgresql.Driver";
    private static String USER;
    private static String PASSWORD;
    private String pass= "src/main/resources/sql/connection.properties";

    /**
     * Constructor for the DatabaseConnection class.
     * @param filePath The path for the .properties file in string format.
     */
    private DatabaseConnection(String filePath) {
        // load a properties file
        try {
            InputStream input = new FileInputStream(filePath);
            properties.load(input);
            logger.info("Properties file has been loaded.");
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setup();
    }

    /**
     * The method is written with Singleton pattern, creates a DatabaseConnection instance if it doesn't exists,
     * returns the existing one otherwise.
     * @param filePath The path for the .properties file in string format.
     * @return Returns the DatabaseConnection instance.
     */
    public static DatabaseConnection getInstance(String filePath) {
        if (instance == null) {
            logger.debug("DatabaseConnection instance is new.");
            instance = new DatabaseConnection(filePath);
        }
        logger.debug("DatabaseCOnnection instance has already been created.");
        return instance;
    }

    /**
     * Sets up the database credentials from the .properties file.
     */
    private static void setup() {
        host = properties.getProperty("url");
        logger.debug("Host of the database is: {}.", host);
        databaseName = properties.getProperty("database");
        DB_URL = "jdbc:postgresql://" + host + "/" + databaseName;
        logger.debug("Database url is: {}.", DB_URL);
        USER = properties.getProperty("user");
        logger.debug("User of the databse is: {}.", USER);
        PASSWORD = properties.getProperty("password");
    }

    /**
     * Connects to the database by registering the JDBC driver and opening the connection.
     * @return Returns the Connection object.
     */
    public Connection getConnection() throws ConnectToStorageFailed{
        Connection connection = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            logger.info("Connecting to database...");

            //STEP 3: Open a connection
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        } catch (SQLException se) {
            //Handle errors for JDBC
            throw new ConnectToStorageFailed(se.getMessage());
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Setting up a prepared statement on a given connection with the given parameters.
     * @param conn A Connection object.
     * @param infos List of information which needs to be set up in the sql query with.
     * @param sql An sql query in string format.
     * @return Returns a prepared statement to execute in other methods.
     * @throws SQLException
     */
    public PreparedStatement createAndSetPreparedStatement(Connection conn, List<Object> infos, String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        logger.debug("SQL query to be set is: {}.", ps);
        for (int i = 0; i < infos.size(); i++) {
            int ColumnIndex = i + 1;
            logger.debug("ColumnIndex is: {}.", ColumnIndex);
            Object actualInfo = infos.get(i);
            if (actualInfo instanceof String) {
                logger.debug("Actual info is a string.");
                ps.setString(ColumnIndex, actualInfo.toString());
            } else if (actualInfo instanceof Integer) {
                logger.debug("Actual info is an integer.");
                ps.setInt(ColumnIndex, (int) actualInfo);
            } else if (actualInfo instanceof Float) {
                logger.debug("Actual info is a float.");
                ps.setFloat(ColumnIndex, (float) actualInfo);
            }
        }
        return ps;
    }
}
