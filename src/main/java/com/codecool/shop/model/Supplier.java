package com.codecool.shop.model;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Supplier extends BaseModel {
    private static final Logger logger = LoggerFactory.getLogger(Supplier.class);
    private ArrayList<Product> products;

    public Supplier(String name, String description) {
        super(name, description);
        this.products = new ArrayList<>();
        logger.debug("New supplier has been created: name: {}, description: {}", name, description);
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        logger.debug("Product list of {} has been set to: {}", this.getName(), products);
    }

    public ArrayList getProducts() {
        return this.products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
        logger.debug("{} has been added to product list of {}", product.getName(), this.getName());
    }

    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "description: %3$s",
                this.id,
                this.name,
                this.description
        );
    }
}