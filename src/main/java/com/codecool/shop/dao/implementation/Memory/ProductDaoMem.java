package com.codecool.shop.dao.implementation.Memory;


import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductDaoMem implements ProductDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoMem.class);

    private List<Product> DATA = new ArrayList<>();
    private static ProductDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoMem() {
    }

    /**
     * Singleton constructor
     * @return ProductDaomem
     */
    public static ProductDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductDaoMem();
            logger.info("ProductDaoMem instantiated");
        }
        return instance;
    }

    /**
     * Adds products to memory.
     * @param product
     */
    @Override
    public void add(Product product) {
        product.setId(DATA.size() + 1);
        DATA.add(product);
        logger.info("{} added to DATA", product);
    }

    /**
     * Finds product by id in memory
     * @param id
     * @return product
     */
    @Override
    public Product find(int id) {
        logger.info("Found product is: {}", DATA.stream().filter(t -> t.getId() == id).toString());
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /**
     * Removes product by id from memory.
     * @param id
     */
    @Override
    public void remove(int id) {
        logger.info("The product to remove is: {}", find(id).toString());
        DATA.remove(find(id));
    }

    /**
     * Gets all product from memory.
     * @return List of products
     */
    @Override
    public List<Product> getAll() {
        logger.info("All products: {}", DATA.toString());
        return DATA;
    }

    /**
     * Gets all product for a given suppliers.
     * @param supplier
     * @return List of products
     */
    @Override
    public List<Product> getBy(Supplier supplier) {
        logger.info("The products by supplier are: {}", DATA.stream().filter(t -> t.getSupplier().equals(supplier)).collect(Collectors.toList()).toString());

        return DATA.stream().filter(t -> t.getSupplier().equals(supplier)).collect(Collectors.toList());
    }

    /**
     * Gets all product for a given category
     * @param productCategory
     * @return list of products
     */
    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        logger.info("The products by product category are {}", DATA.stream().filter(t -> t.getProductCategory().equals(productCategory)).collect(Collectors.toList()).toString());
        return DATA.stream().filter(t -> t.getProductCategory().equals(productCategory)).collect(Collectors.toList());
    }
}
