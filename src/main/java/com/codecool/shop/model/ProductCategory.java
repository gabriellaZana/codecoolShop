package com.codecool.shop.model;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.SupplierDaoJDBC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.util.*;


public class ProductCategory extends BaseModel {
    private static final Logger logger = LoggerFactory.getLogger(ProductCategory.class);
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
        logger.info("{} is added", product.getName());
    }

    public Set<Supplier> getSuppliers() {
        Set<Supplier> suppliers = new HashSet<>();
        List<Product> products = this.getProducts();
        List<Integer> supplierIds = new ArrayList<>();
        SupplierDaoJDBC supplierDaoJDBC = SupplierDaoJDBC.getInstance();
        for (Product product: products) {
            if (!supplierIds.contains(product.getSupplier().getId())) {
                supplierIds.add(product.getSupplier().getId());
            }
        }

        for (Integer id:supplierIds) {
            Supplier supplier = supplierDaoJDBC.find(id);
            suppliers.add(supplier);
        }
        logger.info("Suppliers from getSuppliers method returns: {}", suppliers);
        return suppliers;
    }

    public List<Product> getProducts() {
        ProductDaoJDBC productDaoJDBC = ProductDaoJDBC.getInstance();
        logger.info("Products from getProducts returns: {}", productDaoJDBC.getBy(this));   
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