package com.codecool.shop.model;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.SupplierDaoJDBC;

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


    public void addProduct(Product product) {
        this.products.add(product);
    }

    public Set<Supplier> getSuppliers() {
        Set<Supplier> suppliers = new HashSet<>();
        List<Product> products = this.getProducts();
        List<Integer> supplierIds = new ArrayList<>();
        SupplierDaoJDBC supplierDaoJDBC = SupplierDaoJDBC.getInstance();
        for (Product product: products) {
            supplierIds.add(product.getSupplier().getId());
            if(Collections.frequency(supplierIds, product.getSupplier().getId()) == 1){
                suppliers.add(supplierDaoJDBC.find(product.getSupplier().getId()));
            }
        }
        return suppliers;
    }

    public List<Product> getProducts() {
        ProductDaoJDBC productDaoJDBC = ProductDaoJDBC.getInstance();
        return productDaoJDBC.getBy(this);
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