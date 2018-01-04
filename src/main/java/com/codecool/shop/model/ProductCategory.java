package com.codecool.shop.model;

import java.util.*;


public class ProductCategory extends BaseModel {
    private ArrayList<Product> products;

    public ProductCategory(String name, String description) {
        super(name);
        this.products = new ArrayList<>();
        this.description = description;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Product> getProducts() {
        return this.products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public Set<Supplier> getSuppliers() {
        Set<Supplier> suppliers = new HashSet<>();
        List<Product> products = this.getProducts();
        for (Product product: products) {
            suppliers.add(product.getSupplier());
        }
        return suppliers;
    }

    public String toString() {
        return String.format(
                "id: %1$d," +
                        "name: %2$s, " +
                        "description: %3$s",
                this.id,
                this.name,
                this.description);
    }

}