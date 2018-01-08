package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.utils.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJDBC implements ProductCategoryDao {

    private static ProductCategoryDaoJDBC instance = null;
    private String filePath = "src/main/resources/sql/connection.properties";

    private ProductCategoryDaoJDBC() {
    }

    public static ProductCategoryDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC();
        }
        return instance;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void add(ProductCategory category) throws SQLException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);
        Connection connection = databaseConnection.getConnection();

        String query = "INSERT INTO product_categories (name, description) VALUES (?, ?);";
        if (find(category.getName()) == null) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());


            preparedStatement.executeUpdate();
        }
    }

    @Override
    public ProductCategory find(int id) throws SQLException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);
        Connection connection = databaseConnection.getConnection();

        String query = "SELECT * FROM product_categories WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            ProductCategory productCategory = new ProductCategory(resultSet.getString("name"), resultSet.getString("description"));
            productCategory.setId(resultSet.getInt("id"));
            return productCategory;
        }
        return null;
    }

    public ProductCategory find(String name) throws SQLException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);
        Connection connection = databaseConnection.getConnection();

        String query = "SELECT * FROM product_categories WHERE name=?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            ProductCategory productCategory = new ProductCategory(resultSet.getString("name"), resultSet.getString("description"));
            productCategory.setId(resultSet.getInt("id"));
            return productCategory;
        }
        return null;
    }

    @Override
    public void remove(int id) throws SQLException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);
        Connection connection = databaseConnection.getConnection();

        String query = "DELETE FROM product_categories WHERE id = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();

    }

    @Override
    public List<ProductCategory> getAll() throws SQLException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);
        Connection connection = databaseConnection.getConnection();

        String query = "SELECT * FROM product_categories;";

        List<ProductCategory> productCategories = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            ProductCategory productCategory = new ProductCategory(resultSet.getString("name"), resultSet.getString("description"));
            productCategory.setId(resultSet.getInt("id"));
            productCategories.add(productCategory);
        }
        //System.out.println(productCategories.size());
        return productCategories;
    }
}
