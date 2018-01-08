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
                 PreparedStatement statement = createAndSetPreparedStatement(connection, infos, addQuery)) {
                statement.executeUpdate();
            }
        }
    }

    @Override
    public ProductCategory find(int id) {
        String getProductQuery = "SELECT * FROM product_categories WHERE id=?";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        return executeFindQuery(getProductQuery, infos);
    }

    public ProductCategory find(String name){
        String getProductQuery = "SELECT * FROM product_categories WHERE name=?";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(name));
        return executeFindQuery(getProductQuery, infos);
    }


    private ProductCategory executeFindQuery(String query, ArrayList<Object> infos) {
        ProductCategory resultProductCategory = null;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = createAndSetPreparedStatement(connection, infos, query);
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
    public void remove(int id) throws SQLException{
        String removeProductQuery = "DELETE FROM product_categories WHERE id = ?;";

        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = createAndSetPreparedStatement(connection, infos, removeProductQuery)){
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
                productCategoryList.add(productCategory);
            }
        } 
        return productCategoryList;
    }


    private PreparedStatement createAndSetPreparedStatement(Connection conn, List<Object> infos, String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < infos.size(); i++) {
            int ColumnIndex = i+1;
            Object actualInfo = infos.get(i);
            if(actualInfo instanceof String) {
                ps.setString(ColumnIndex, actualInfo.toString());
            }
            else if (actualInfo instanceof Integer) {
                ps.setInt(ColumnIndex, (int) actualInfo);
            }
            else if (actualInfo instanceof Float) {
                ps.setFloat(ColumnIndex, (float) actualInfo);
            }
        }
        return ps;
    }
}
