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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProductCategoryDaoJDBC implements ProductCategoryDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryDaoJDBC.class);

    private static ProductCategoryDaoJDBC instance = null;
    private String filePath = "src/main/resources/sql/connection.properties";
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);

    private ProductCategoryDaoJDBC() {
    }

    public static ProductCategoryDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC();
            logger.debug("A new instance has been created.");
        }
        return instance;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        logger.debug("FilePath has been set to {}.", filePath);
    }

    @Override
    public void add(ProductCategory category) throws SQLException{
        String addQuery = "INSERT INTO product_categories (name, description) VALUES (?, ?);";
        ArrayList<Object> infos = new ArrayList<>(Arrays.asList(category.getName(), category.getDescription()));
        logger.debug("Infos list while adding product category is: {}.", infos);
        if (find(category.getName()) == null) {
            logger.info("Category is new in the database.");
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, addQuery)) {
                logger.debug("The query for adding product category is: {}.", statement);
                statement.executeUpdate();
                logger.info("Product category - {} - has been added.", category.getName());
            }
        }
        logger.warn("Product category is already in database with name {}.", category.getName());
    }

    @Override
    public ProductCategory find(int id) throws SQLException{
        String getProductQuery = "SELECT * FROM product_categories WHERE id=?";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        logger.debug("Infos list while trying to find product category is: {}.", infos);
        return executeFindQuery(getProductQuery, infos);
    }

    public ProductCategory find(String name) throws SQLException{
        String getProductQuery = "SELECT * FROM product_categories WHERE name=?";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(name));
        logger.debug("Infos list while trying to find product category by name is: {}.", infos);
        return executeFindQuery(getProductQuery, infos);
    }


    private ProductCategory executeFindQuery(String query, ArrayList<Object> infos) throws SQLException {
        ProductCategory resultProductCategory = null;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, query);
             ResultSet result = statement.executeQuery()) {
            logger.debug("The query for finding product category is: {}.", statement);

            while (result.next()) {
                resultProductCategory = new ProductCategory(result.getString("name"),
                                                            result.getString("description"));
                resultProductCategory.setId(result.getInt("id"));
                logger.debug("The id of the result product category is: {}.", result.getInt("id"));
            }
        }
        return resultProductCategory;
    }



    @Override
    public void remove(int id) throws SQLException {
        String removeProductQuery = "DELETE FROM product_categories WHERE id = ?;";

        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        logger.debug("Infos list for removing product category is: {}.", infos);
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, removeProductQuery)){
            statement.executeUpdate();
            logger.warn("Product category with id ({}) has been removed.", id);
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
                logger.debug("Id of the found product category is: {}.", result.getInt("id"));
                productCategoryList.add(productCategory);
                logger.debug("Product category list at the moment is: {}.", productCategoryList);
            }
        }
        return productCategoryList;
    }
}
