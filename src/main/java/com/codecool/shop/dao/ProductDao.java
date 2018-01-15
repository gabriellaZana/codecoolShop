package com.codecool.shop.dao;

import com.codecool.shop.model.Supplier;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {

    /**
     * Adds product
     * @param product
     * @throws SQLException
     */
    void add(Product product) throws SQLException;

    /**
     * finds product by id
     * @param id
     * @return Product
     * @throws SQLException
     */
    Product find(int id) throws SQLException;

    /**
     * Removes product by id
     * @param id
     * @throws SQLException
     */
    void remove(int id) throws SQLException;

    /**
     * Gets all products
     * @return list of products
     * @throws SQLException
     */
    List<Product> getAll() throws SQLException;

    /**
     * Gets products from a supplier
     * @param supplier
     * @return list of products
     * @throws SQLException
     */
    List<Product> getBy(Supplier supplier) throws SQLException;

    /**
     * Gets products by product category
     * @param productCategory
     * @return list of products
     * @throws SQLException
     */
    List<Product> getBy(ProductCategory productCategory) throws SQLException;

}
