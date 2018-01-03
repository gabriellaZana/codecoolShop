package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {
    private static ProductDaoJDBC instance = null;
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private Connection connection = databaseConnection.getConnection();
    private PreparedStatement statement = null;

    private ProductDaoJDBC() {

    }

    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }


    @Override
    public void add(Product product) {
        SupplierDaoJDBC supplierDaoJDBC = SupplierDaoJDBC.getInstance();
        ProductCategoryDaoJDBC productCategoryDaoJDBC = ProductCategoryDaoJDBC.getInstance();
        if (find(product.getName()) != null) {
            return;
        }
        try {
            String addQuery = "INSERT INTO products (name, description, price, product_category_id, supplier_id, currency) " +
                    "VALUES (?, ?, ?, ?, ?, ?);";
            statement = connection.prepareStatement(addQuery);

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setFloat(3, product.getDefaultPrice());
            statement.setInt(4, productCategoryDaoJDBC.find(product.getProductCategory().getName()).getId());
            statement.setInt(5, supplierDaoJDBC.find(product.getSupplier().getName()).getId());
            statement.setString(6, product.getDefaultCurrency().toString());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Product find(int id) {
        Product resultProduct = null;
        try {
            String getProductQuery = "SELECT * FROM products WHERE id=?;";
            statement = connection.prepareStatement(getProductQuery);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

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


    public Product find(String name){
        Product resultProduct = null;
        try {
            String getProductQuery = "SELECT * FROM products WHERE name=?;";
            statement = connection.prepareStatement(getProductQuery);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();

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
        try {
            String removeProductQuery = "DELETE FROM products WHERE id=?;";
            statement = connection.prepareStatement(removeProductQuery);
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Product> getAll() {
        List<Product> productList = new ArrayList<>();
        try {
            String getProductsQuery = "SELECT * FROM products;";
            statement = connection.prepareStatement(getProductsQuery);
            ResultSet result = statement.executeQuery();
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
        try {
            String getProductsBySupplierQuery = "SELECT * FROM products WHERE supplier_id=?;";
            statement = connection.prepareStatement(getProductsBySupplierQuery);
            statement.setInt(1, supplier.getId());
            ResultSet result = statement.executeQuery();
            ProductCategoryDao category = ProductCategoryDaoJDBC.getInstance();
            SupplierDao supplierJDBC = SupplierDaoJDBC.getInstance();

            while (result.next()) {
                Product product = new Product(result.getString("name"), result.getFloat("price"),
                        result.getString("currency"), result.getString("description"),
                        category.find(result.getInt("product_category_id")), supplierJDBC.find(result.getInt("supplier_id")));
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
        try {
            String getProductsByCategoryQuery = "SELECT * FROM products WHERE product_category_id=?;";
            statement = connection.prepareStatement(getProductsByCategoryQuery);
            statement.setInt(1, productCategory.getId());
            ResultSet result = statement.executeQuery();
            ProductCategoryDao category = ProductCategoryDaoJDBC.getInstance();
            SupplierDao supplier = SupplierDaoJDBC.getInstance();

            while (result.next()) {
                Product product = new Product(result.getString("name"), result.getFloat("price"),
                        result.getString("currency"), result.getString("description"),
                        category.find(result.getInt("product_category_id")), supplier.find(result.getInt("supplier_id")));
                productListByCategory.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productListByCategory;
    }
}
