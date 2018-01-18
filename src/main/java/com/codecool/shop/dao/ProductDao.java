package com.codecool.shop.dao;

import com.codecool.shop.exception.ConnectToStorageFailed;
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
    void add(Product product) throws ConnectToStorageFailed;

    /**
     * finds product by id
     * @param id
     * @return Product
     * @throws SQLException
     */
    Product find(int id) throws ConnectToStorageFailed;

    /**
     * Removes product by id
     * @param id
     * @throws SQLException
     */
    void remove(int id) throws ConnectToStorageFailed;

    /**
     * Gets all products
     * @return list of products
     * @throws SQLException
     */
    List<Product> getAll() throws ConnectToStorageFailed;

    /**
     * Gets products from a supplier
     * @param supplier
     * @return list of products
     * @throws SQLException
     */
    List<Product> getBy(Supplier supplier) throws ConnectToStorageFailed;

    /**
     * Gets products by product category
     * @param productCategory
     * @return list of products
     * @throws SQLException
     */
    List<Product> getBy(ProductCategory productCategory) throws ConnectToStorageFailed;

}
