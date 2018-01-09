package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.utils.DatabaseConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoJDBC.class);
    private static ProductDaoJDBC instance = null;
    private String filePath = "src/main/resources/sql/connection.properties";
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);

    private ProductDaoJDBC() {

    }

    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
            logger.debug("A new instance of {} has been created", ProductDaoJDBC.class.getSimpleName());
        }
        return instance;
    }

    public void setFilePath(String filePath) {
        String oldFilePath = this.filePath;
        this.filePath = filePath;
        logger.debug("Filepath has been set to {} from {}",this.filePath, oldFilePath);
    }


    @Override
    public void add(Product product) throws SQLException {
        SupplierDaoJDBC supplierDaoJDBC = SupplierDaoJDBC.getInstance();
        ProductCategoryDaoJDBC productCategoryDaoJDBC = ProductCategoryDaoJDBC.getInstance();
        if (find(product.getName()) != null) {
            return;
        }
        String addQuery = "INSERT INTO products (name, description, price, product_category_id, supplier_id, currency) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        ArrayList<Object> infos = new ArrayList<>(Arrays.asList(product.getName(), product.getDescription(), product.getDefaultPrice(),
                productCategoryDaoJDBC.find(product.getProductCategory().getName()).getId(), supplierDaoJDBC.find(product.getSupplier().getName()).getId(),
                product.getDefaultCurrency().toString()));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, addQuery)) {
            statement.executeUpdate();
        }
    }


    @Override
    public Product find(int id) throws SQLException {
        String getProductQuery = "SELECT * FROM products WHERE id=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        return executeFindQuery(getProductQuery, infos);
    }


    public Product find(String name) throws SQLException {
        String getProductByNameQuery = "SELECT * FROM products WHERE name=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(name));
        return executeFindQuery(getProductByNameQuery, infos);
    }

    private Product executeFindQuery(String query, ArrayList<Object> infos) throws SQLException {
        Product resultProduct = null;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, query);
             ResultSet result = statement.executeQuery()) {

            ProductCategoryDao category = ProductCategoryDaoJDBC.getInstance();
            SupplierDao supplier = SupplierDaoJDBC.getInstance();
            while (result.next()) {
                resultProduct = new Product(result.getString("name"), result.getFloat("price"),
                        result.getString("currency"), result.getString("description"),
                        category.find(result.getInt("product_category_id")), supplier.find(result.getInt("supplier_id")));
                resultProduct.setId(result.getInt("id"));
            }
        }
        return resultProduct;
    }

    @Override
    public void remove(int id) throws SQLException {
        String removeProductQuery = "DELETE FROM products WHERE id=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, removeProductQuery)) {
            statement.executeUpdate();
        }
    }


    @Override
    public List<Product> getAll() throws SQLException {
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
        }
        return productList;
    }


    @Override
    public List<Product> getBy(Supplier supplier) throws SQLException {
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
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, getProductsBySupplierQuery);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Product product = new Product(result.getString("name"),
                        result.getFloat("price"),
                        result.getString("currency"),
                        result.getString("description"),
                        new ProductCategory(result.getString("product_category_name"),
                                result.getString("product_category_desc")) {{
                            setId(result.getInt("product_category_id"));
                        }},
                        new Supplier(result.getString("supplier_name"),
                                result.getString("supplier_desc")) {{
                            setId(result.getInt("supplier_id"));
                        }});
                productListBySupplier.add(product);
            }
        }
        return productListBySupplier;
    }


    @Override
    public List<Product> getBy(ProductCategory productCategory) throws SQLException {
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
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, getProductsByCategoryQuery);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Product product = new Product(result.getString("name"),
                        result.getFloat("price"),
                        result.getString("currency"),
                        result.getString("description"),
                        new ProductCategory(result.getString("product_category_name"),
                                result.getString("product_category_desc")) {{
                            setId(result.getInt("product_category_id"));
                        }},
                        new Supplier(result.getString("supplier_name"),
                                result.getString("supplier_desc")) {{
                            setId(result.getInt("supplier_id"));
                        }});
                product.setId(result.getInt("id"));
                productListByCategory.add(product);
            }
        }
        return productListByCategory;
    }
}


