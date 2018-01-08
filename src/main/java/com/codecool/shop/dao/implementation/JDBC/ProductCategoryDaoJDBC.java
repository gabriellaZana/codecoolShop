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

public class ProductCategoryDaoJDBC implements ProductCategoryDao {

    private static ProductCategoryDaoJDBC instance = null;
    private String filePath = "src/main/resources/sql/connection.properties";
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);

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

    @Override
    public ProductCategory find(int id) throws SQLException{
        String getProductQuery = "SELECT * FROM product_categories WHERE id=?";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        return executeFindQuery(getProductQuery, infos);
    }

    public ProductCategory find(String name) throws SQLException{
        String getProductQuery = "SELECT * FROM product_categories WHERE name=?";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(name));
        return executeFindQuery(getProductQuery, infos);
    }


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



    @Override
    public void remove(int id) throws SQLException {
        String removeProductQuery = "DELETE FROM product_categories WHERE id = ?;";

        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, removeProductQuery)){
            statement.executeUpdate();
        }
    }

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
