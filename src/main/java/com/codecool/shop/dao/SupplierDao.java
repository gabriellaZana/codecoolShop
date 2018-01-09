package com.codecool.shop.dao;

import com.codecool.shop.model.Supplier;

import java.sql.SQLException;
import java.util.List;

/**
 * SupplierDao Interface
 * <p>Sets the methods, which needs to be implemented, for the Supplier DataAccessObject implementations.</p>
 */
public interface SupplierDao {

    /**
     * Adds a Supplier object to a storage (memory or database).
     * @param supplier An instance of a Supplier class.
     * @throws SQLException
     */
    void add(Supplier supplier) throws SQLException;

    /**
     * Finds a supplier in a storage by id.
     * @param id A unique number for identifying the supplier in the storage.
     * @return Returns the found Supplier object.
     * @throws SQLException
     */
    Supplier find(int id) throws SQLException;

    /**
     * Removes the supplier from the storage with the given id.
     * @param id A unique number for identifying the supplier in the storage.
     * @throws SQLException
     */
    void remove(int id) throws SQLException;

    /**
     * @return Returns all suppliers in a list, which can be found in the storage.
     * @throws SQLException
     */
    List<Supplier> getAll() throws SQLException;
}
