package com.codecool.shop.model;

import java.util.ArrayList;

/**
 * Supplier object
 * connected to Products
 * @author Javengers
 * @version 1.0
 */
public class Supplier extends BaseModel {
    /** products of the Supplier */
    private ArrayList<Product> products;

    /**
     * Constructos
     * @param name name of the Supplier
     * @param description short description of the Supplier
     */
    public Supplier(String name, String description) {
        super(name, description);
        this.products = new ArrayList<>();
    }


    /**
     * Sets the products field of the object to products list parameter.
     * @param products List of Product objects
     * @see Product
     */
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }


    /**
     * Returns the products of the Supplier object.
     * @return ArrayList of products.
     * @see Product
     */
    public ArrayList<Product> getProducts() {
        return this.products;
    }


    /**
     * Adds the given Product to the products field of the Supplier object.
     * @param product Product object to be added.
     * @see Product
     */
    public void addProduct(Product product) {
        this.products.add(product);
    }


    /**
     * Displays the attributes of Supplier object in a pretty format.
     * @return String format of the Supplier object
     */
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