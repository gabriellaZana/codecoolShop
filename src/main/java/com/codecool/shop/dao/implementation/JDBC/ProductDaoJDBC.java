package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {
    private static ProductDaoJDBC instance = null;
    private String filePath = "src/main/resources/sql/connection.properties";
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);

    private ProductDaoJDBC() {

    }

    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public void add(Product product) {
        SupplierDaoJDBC supplierDaoJDBC = SupplierDaoJDBC.getInstance();
        ProductCategoryDaoJDBC productCategoryDaoJDBC = ProductCategoryDaoJDBC.getInstance();
        if (find(product.getName()) != null) {
            return;
        }
        String addQuery = "INSERT INTO products (name, description, price, product_category_id, supplier_id, currency) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        ArrayList<Object> infos = new ArrayList<>(Arrays.asList(product.getName(),product.getDescription(), product.getDefaultPrice(),
                productCategoryDaoJDBC.find(product.getProductCategory().getName()).getId(), supplierDaoJDBC.find(product.getSupplier().getName()).getId(),
                product.getDefaultCurrency().toString()));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = createAndSetPreparedStatement(connection, infos, addQuery)){
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setFloat(3, product.getDefaultPrice());
            statement.setInt(4, productCategoryDaoJDBC.find(product.getProductCategory().getName()).getId());
            statement.setInt(5, supplierDaoJDBC.find(product.getSupplier().getName()).getId());
            statement.setString(6, product.getDefaultCurrency().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Product find(int id) {
        String getProductQuery = "SELECT * FROM products WHERE id=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        return executeFindQuery(getProductQuery, infos);
    }


    public Product find(String name){
        String getProductByNameQuery = "SELECT * FROM products WHERE name=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(name));
        return executeFindQuery(getProductByNameQuery, infos);
    }

    private Product executeFindQuery(String query, ArrayList<Object> infos) {
        Product resultProduct = null;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = createAndSetPreparedStatement(connection, infos, query);
             ResultSet result = statement.executeQuery()) {

            ProductCategoryDao category = ProductCategoryDaoJDBC.getInstance();
            SupplierDao supplier = SupplierDaoJDBC.getInstance();
            while (result.next()) {
                resultProduct = new Product(result.getString("name"), result.getFloat("price"),
                        result.getString("currency"), result.getString("description"),
                        category.find(result.getInt("product_category_id")), supplier.find(result.getInt("supplier_id")));
                resultProduct.setId(result.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultProduct;
    }

    @Override
    public void remove(int id) {
        String removeProductQuery = "DELETE FROM products WHERE id=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = createAndSetPreparedStatement(connection, infos, removeProductQuery)){
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Product> getAll() {
        List<Product> productList = new ArrayList<>();
        String getProductsQuery = "SELECT * FROM products;";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(getProductsQuery);
             ResultSet result = statement.executeQuery()) {
            ProductCategoryDao category = ProductCategoryDaoJDBC.getInstance();
            SupplierDao supplier = SupplierDaoJDBC.getInstance();

            while (result.next()) {
                Product product = new Product(result.getString("name"), result.getFloat("price"),
                        result.getString("currency"), result.getString("description"),
                        category.find(result.getInt("product_category_id")), supplier.find(result.getInt("supplier_id")));
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }


    @Override
    public List<Product> getBy(Supplier supplier) {
        List<Product> productListBySupplier = new ArrayList<>();
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(supplier.getId()));
        String getProductsBySupplierQuery = "SELECT products.id," +
                "  products.name," +
                "  products.description," +
                "  products.price," +
                "  products.currency," +
                "  product_categories.id AS product_category_id," +
                "  product_categories.name AS product_category_name," +
                "  product_categories.description AS product_category_desc," +
                "  suppliers.id AS supplier_id," +
                "  suppliers.name AS supplier_name," +
                "  suppliers.description AS supplier_desc " +
                "FROM products " +
                "JOIN product_categories ON products.product_category_id=product_categories.id " +
                "JOIN suppliers ON products.supplier_id=suppliers.id " +
                "WHERE supplier_id=?;";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = createAndSetPreparedStatement(connection, infos, getProductsBySupplierQuery);
             ResultSet result = statement.executeQuery()){
            while (result.next()) {
                Product product = new Product(result.getString("name"),
                                                result.getFloat("price"),
                                                result.getString("currency"),
                                                result.getString("description"),
                        new ProductCategory(result.getString("product_category_name"),
                                            result.getString("product_category_desc")),
                        new Supplier(result.getString("supplier_name"),
                                    result.getString("supplier_desc")));
                productListBySupplier.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productListBySupplier;
    }


    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        List<Product> productListByCategory = new ArrayList<>();
        String getProductsByCategoryQuery = "SELECT products.id," +
                "  products.name," +
                "  products.description," +
                "  products.price," +
                "  products.currency," +
                "  product_categories.id AS product_category_id," +
                "  product_categories.name AS product_category_name," +
                "  product_categories.description AS product_category_desc," +
                "  suppliers.id AS supplier_id," +
                "  suppliers.name AS supplier_name," +
                "  suppliers.description AS supplier_desc " +
                "FROM products " +
                "JOIN product_categories ON products.product_category_id=product_categories.id " +
                "JOIN suppliers ON products.supplier_id=suppliers.id " +
                "WHERE product_category_id=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(productCategory.getId()));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = createAndSetPreparedStatement(connection, infos, getProductsByCategoryQuery);
             ResultSet result = statement.executeQuery()){
            while (result.next()) {
                //System.out.println(result.getInt("id"));
                Product product = new Product(result.getString("name"),
                        result.getFloat("price"),
                        result.getString("currency"),
                        result.getString("description"),
                        new ProductCategory(result.getString("product_category_name"),
                                result.getString("product_category_desc")){{ setId(result.getInt("product_category_id"));}},
                        new Supplier(result.getString("supplier_name"),
                                result.getString("supplier_desc")){{ setId(result.getInt("supplier_id"));}});
                product.setId(result.getInt("id"));
                productListByCategory.add(product);
            }
            //System.out.println(productListByCategory.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productListByCategory;
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
