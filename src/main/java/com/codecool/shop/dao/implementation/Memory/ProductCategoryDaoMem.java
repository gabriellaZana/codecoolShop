package com.codecool.shop.dao.implementation.Memory;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoMem implements ProductCategoryDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryDaoMem.class);
    private List<ProductCategory> DATA = new ArrayList<>();
    private static ProductCategoryDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductCategoryDaoMem() {
    }

    public static ProductCategoryDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoMem();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        category.setId(DATA.size() + 1);
        DATA.add(category);
        logger.info("{} has been saved to memory",category.getName());
    }

    @Override
    public ProductCategory find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        DATA.remove(find(id));
        logger.info("Supplier with id {} has been removed from memory.", id);
    }

    @Override
    public List<ProductCategory> getAll() {
        return DATA;
    }
}
