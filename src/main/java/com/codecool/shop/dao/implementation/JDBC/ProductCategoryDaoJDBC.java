package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.utils.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * ProductCategory class which implements the ProductCategory Interface with database.
 */
public class ProductCategoryDaoJDBC implements ProductCategoryDao {

    private static ProductCategoryDaoJDBC instance = null;
    private String filePath = "src/main/resources/sql/connection.properties";
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);

    private ProductCategoryDaoJDBC() {
    }

    /**
         * The method is written with Singleton pattern, creates a ProductCategoryDaoJDBC instance if it doesn't exists,
         * returns the existing one otherwise.
     * @return Returns the ProductCategoryDaoJDBC instance.
     */
    public static ProductCategoryDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC();
        }
        return instance;
    }

    /**
     * Sets the filePath of the instance so it can reach the .properties file.
     * @param filePath The path for the .properties file in string format.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Adds the given ProductCategory object to the database by getting a connection, setting up a preparedStatement and
     * executing the query.
     * @param category The ProductCategory object which needs to be added.
     */
    @Override
    public void add(ProductCategory category) throws SQLException{
        String addQuery = "INSERT INTO product_categories (name, description) VALUES (?, ?);";
        ArrayList<Object> infos = new ArrayList<>(Arrays.asList(category.getName(), category.getDescription()));
        if (find(category.getName()) == null) {
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, addQuery)) {
                statement.executeUpdate();
            }
        }
    }

    /**
     * Finds the product category with the given id in the database.
     * @param id A unique number for identifying the product category in the database.
     * @return Returns the ProductCategory object if found, <code>null</code> otherwise.
     */
    @Override
    public ProductCategory find(int id) throws SQLException{
        String getProductQuery = "SELECT * FROM product_categories WHERE id=?";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        return executeFindQuery(getProductQuery, infos);
    }

    /**
     * Finds the product category with the given name in the database.
     * @param name The name of the product category which needs to be find.
     * @return Returns the ProductCategory object if found, <code>null</code> otherwise.
     */
    public ProductCategory find(String name) throws SQLException{
        String getProductQuery = "SELECT * FROM product_categories WHERE name=?";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(name));
        return executeFindQuery(getProductQuery, infos);
    }

    /**
     * Helps to execute the find queries by getting the connection, setting up a prepared statement, because of their
     * different given parameters.
     * @param query The query which needs to be set up, in string format.
     * @param infos A list, which has the id or name in object format.
     * @return Returns the found product category.
     * @throws SQLException
     */
    private ProductCategory executeFindQuery(String query, ArrayList<Object> infos) throws SQLException {
        ProductCategory resultProductCategory = null;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, query);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                resultProductCategory = new ProductCategory(result.getString("name"),
                                                            result.getString("description"));
                resultProductCategory.setId(result.getInt("id"));
            }
        }
        return resultProductCategory;
    }


    /**
     * Removes the product category from the database with the given id by getting a connection, setting up a prepared
     * statement and executing a query.
     * @param id A unique number for identifying the product category in the database.
     */
    @Override
    public void remove(int id) throws SQLException {
        String removeProductQuery = "DELETE FROM product_categories WHERE id = ?;";

        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, removeProductQuery)){
            statement.executeUpdate();
        }
    }


    /**
     * Getting a connection, setting up a prepared statement, executing a query and collecting all the product categories
     * from the database.
     * @return Returns all the ProductCategory objects in a list, which can be empty as well.
     */
    @Override
    public List<ProductCategory> getAll() throws SQLException {
        List<ProductCategory> productCategoryList = new ArrayList<>();
        String getProductCategoriesQuery = "SELECT * FROM product_categories;";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(getProductCategoriesQuery);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                ProductCategory productCategory = new ProductCategory(result.getString("name"),
                                                                      result.getString("description"));
                productCategory.setId(result.getInt("id"));
                productCategoryList.add(productCategory);
            }
        }
        return productCategoryList;
    }
}
