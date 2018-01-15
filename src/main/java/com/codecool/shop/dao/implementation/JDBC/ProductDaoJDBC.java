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

/**
 * Database implementation of ProductDao interface.
 * <p>Singleton class.</p>
 * @author Javengers
 * version 1.0
 */
public class ProductDaoJDBC implements ProductDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoJDBC.class);
    private static ProductDaoJDBC instance = null;

    /**
     * Path to connection.properties file for accessing the database.
     */
    private String filePath = "src/main/resources/sql/connection.properties";

    /**
     * DatabaseConnection object providing database utilities.
     */
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);

    /**
     * Constructor
     */
    private ProductDaoJDBC() {
    }


    /**
     * Creates a single ProductDaoJDBC instance if it doesn't exist yet,
     * returns the existing one otherwise.
     * @return ProductDaoJDBC object.
     */
    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
            logger.debug("A new instance of {} has been created", ProductDaoJDBC.class.getSimpleName());
        }
        return instance;
    }


    /**
     * Sets the filePath field of the object to filePath parameter.
     * @param filePath The path for the connection.properties file in string format.
     */
    public void setFilePath(String filePath) {
        String oldFilePath = this.filePath;
        this.filePath = filePath;
        logger.debug("Filepath has been set to {} from {}",this.filePath, oldFilePath);
    }

    /**
     * Adds the given Product object to the database if it's not there yet.
     * @param product Product object to be inserted to the database.
     * @throws SQLException if either connecting to database, creating prepared statement
     * or the sql execution itself fails.
     * @see Product
     * @see SQLException
     */
    @Override
    public void add(Product product) throws SQLException {
        SupplierDaoJDBC supplierDaoJDBC = SupplierDaoJDBC.getInstance();
        ProductCategoryDaoJDBC productCategoryDaoJDBC = ProductCategoryDaoJDBC.getInstance();
        if (find(product.getName()) != null) {
            logger.debug("Adding {} failed because already in database.", product.getName());
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
            logger.info("{} added successfully to database.", product.getName());
        }
    }

    /**
     * Finds the Product object in the database by id.
     * @param id Unique id of the searched Product in the database.
     * @return the searched Product object if found, <code>null</code> otherwise.
     * @throws SQLException if either connecting to database, creating prepared statement
     * or the sql execution itself fails.
     * @see Product
     * @see SQLException
     */
    @Override
    public Product find(int id) throws SQLException {
        String getProductQuery = "SELECT * FROM products WHERE id=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        return executeFindQuery(getProductQuery, infos);
    }


    /**
     * Finds the Product object in the database by name.
     * @param name Unique name of the searched Product in the database.
     * @return the searched Product object if found, <code>null</code> otherwise.
     * @throws SQLException if either connecting to database, creating prepared statement
     * or the sql execution itself fails.
     * @see Product
     * @see SQLException
     */
    public Product find(String name) throws SQLException {
        String getProductByNameQuery = "SELECT * FROM products WHERE name=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(name));
        return executeFindQuery(getProductByNameQuery, infos);
    }


    /**
     * Helping method for find methods, executes the sql query.
     * @param query The sql query for the prepared statement in string format.
     * @param infos An arrayList of informations to be inserted to the prepared statement,
     *             either id or name of the Product in object format.
     * @return the searched Product object if found, <code>null</code> otherwise.
     * @throws SQLException if either connecting to database, creating prepared statement
     * or the sql execution itself fails.
     * @see SQLException
     */
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
                logger.debug("{} found in database.", resultProduct.getName());
            }
        }
        return resultProduct;
    }


    /**
     * Removes the Product from the database with the given id.
     * @param id Unique id of the Product to be removed in the database.
     * @throws SQLException if either connecting to database, creating prepared statement
     * or the sql execution itself fails.
     * @see SQLException
     */
    @Override
    public void remove(int id) throws SQLException {
        String removeProductQuery = "DELETE FROM products WHERE id=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, removeProductQuery)) {
            statement.executeUpdate();
            logger.info("Product with id: {} has been removed from database", id);
        }
    }


    /**
     * Collects all the Products from the database.
     * @return List of Product objects, or empty List if no Product was found in the database.
     * @throws SQLException if either connecting to database, creating prepared statement
     * or the sql execution itself fails.
     * @see SQLException
     */
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


    /**
     * Collects all the Product in the database with the given supplier.
     * @param supplier A supplier object for the search condition.
     * @return List of Product objects which have the given Supplier
     * or empty List if no Product was found in the database with the given supplier.
     * @throws SQLException if either connecting to database, creating prepared statement
     * or the sql execution itself fails.
     * @see Supplier
     * @see SQLException
     */
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


    /**
     * Collects all the Product in the database with the given product category.
     * @param productCategory A product category object for the search condition.
     * @return List of Product objects which have the given product category
     * or empty List if no Product was found in the database with the given product category.
     * @throws SQLException if either connecting to database, creating prepared statement
     * or the sql execution itself fails.
     * @see ProductCategory
     * @see SQLException
     */
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


