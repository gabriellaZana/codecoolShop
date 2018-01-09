package com.codecool.shop.dao;

import com.codecool.shop.model.ShoppingCart;
import java.util.List;

/**
 * Defines the necessary methods of the various implementations of Data Access Object (DAO)
 * to allocate and interact with data in either memory or database or any other kind of storage.
 *
 * @author codecool
 * @version 1.0
 *
 */

public interface ShoppingCartDao {

    /**
     * Adds the shoppingCart object to a storage.
     * @param shoppingCart Instance of the shoppingCart class.
     */
    void add(ShoppingCart shoppingCart);


    /**
     * Finds the shoppingCart object in the storage by id.
     * @param id representing the unique id for the shoppingCart object in the storage.
     * @return Returns a shoppingCart object.
     */
    ShoppingCart find(int id);


    /**
     * Removes the shoppingCart object from storage by id.
     * @param id representing the unique id of the shoppingCart object to be removed.
     */
    void remove(int id);


    /**
     * Gets all the shoppingCart objects found in storage.
     * @return list of all ShoppingCart objects in storage.
     */
    List<ShoppingCart> getAll();


    /**
     * Removes all shoppingCart objects found in storage.
     */
    void removeAll();
}
