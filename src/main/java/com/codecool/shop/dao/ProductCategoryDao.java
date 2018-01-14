package com.codecool.shop.dao;

import com.codecool.shop.model.ProductCategory;

import java.sql.SQLException;
import java.util.List;

/**
 * ProductCategoryDao Interface
 *
 * <p>sets the default methods for the Data Access Object implementations</p>
 *
 * @author Codecool
 * @version 1.0
 */
public interface ProductCategoryDao {
    /**
     * Adds the ProductCategory to a storage.
     * @param category Instance of the ProductCategory class.
     */
    void add(ProductCategory category) throws SQLException;

    /**
     * Finds the ProductCategory in the storage by id
     * @param id represents the unique id for the ProductCategory in the storage.
     * @return Returns a ProductCategory object.
     */
    ProductCategory find(int id) throws SQLException;

    /**
     * Removes the ProductCategory with the id from the storage.
     * @param id represents the unique id for the ProductCategory in the storage.
     */
    void remove(int id) throws SQLException;

    /**
     * @return Returns all ProductCategory from the storage in a List.
     */
    List<ProductCategory> getAll() throws SQLException;


}
