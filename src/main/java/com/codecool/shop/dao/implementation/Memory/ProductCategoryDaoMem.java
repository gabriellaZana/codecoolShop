package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Memory implementation of ProductCategoryDao interface.
 * <p>Singleton class.</p>
 * @author Javengers
 * version 1.0
 */
public class ProductCategoryDaoMem implements ProductCategoryDao {
    /**
     * Storage of Product category objects in memory.
     */
    private List<ProductCategory> DATA = new ArrayList<>();
    private static ProductCategoryDaoMem instance = null;

    /**
     * Constructor
     */
    private ProductCategoryDaoMem() {
    }

    /**
     * Creates a single ProductCategoryDaoMem instance if it doesn't exist yet,
     * returns the existing one otherwise.
     * @return ProductCategoryDaoMem object.
     */
    public static ProductCategoryDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoMem();
        }
        return instance;
    }


    /**
     * Adds the given ProductCategory object to the DATA field.
     * @param category ProductCategory object to be inserted to the DATA field.
     * @see ProductCategory
     */
    @Override
    public void add(ProductCategory category) {
        category.setId(DATA.size() + 1);
        DATA.add(category);
    }


    /**
     * Searches for the ProductCategory object in the DATA field by id.
     * @param id Unique id of the ProductCategory to be searched.
     * @return the searched ProductCategory object if found, <code>null</code> otherwise.
     * @see ProductCategory
     */
    @Override
    public ProductCategory find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }


    /**
     * Removes the ProductCategory object from the DATA field with the given id.
     * @param id Unique id of the ProductCategory to be removed.
     * @see ProductCategory
     */
    @Override
    public void remove(int id) {
        DATA.remove(find(id));
    }


    /**
     * Collects all ProductCategory objects from the DATA field.
     * @return List of ProductCategory objects saved in DATA field,
     * or empty List if DATA field was empty.
     * @see ProductCategory
     */
    @Override
    public List<ProductCategory> getAll() {
        return DATA;
    }
}
