package com.codecool.shop.model;

import java.util.Currency;

/**
 * Product Object
 *
 * <p>Attributes of product</p>
 *
 * @author Javengers
 * @version 1.0
 */

public class Product extends BaseModel {

    private float defaultPrice;
    private Currency defaultCurrency;
    private ProductCategory productCategory;
    private Supplier supplier;

    /**
     * Constructor
     *
     * @param name (required) The name of the Product. Must have content.
     * @param defaultPrice (required) used to set the Price of the Product.
     * @param currencyString (required) used to set the Price of the Product.
     * @param description (required) A simple description of the Product.
     * @param productCategory (required) Defines what category the Product belongs to.
     * @param supplier (required) Defines which supplier makes the Product.
     */
    public Product(String name, float defaultPrice, String currencyString, String description, ProductCategory productCategory, Supplier supplier) {
        super(name, description);
        this.setPrice(defaultPrice, currencyString);
        this.setSupplier(supplier);
        this.setProductCategory(productCategory);
    }

    /**
     *
     * @return Returns the defaultPrice of the Product.
     */
    public float getDefaultPrice() {
        return defaultPrice;
    }

    /**
     *Set the defaultPrice of the Product.
     *
     * @param defaultPrice float which represents the Product price.
     */
    public void setDefaultPrice(float defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    /**
     * @return Returns the defaultCurrency of the Product.
     */
    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    /**
     * Sets the defaultCurrency of the Product.
     * @param defaultCurrency An object of the Currency class.
     */
    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    /**
     * @return Returns the concatonation of the defaultPrice and the defaultCurrency.
     */
    public String getPrice() {
        return String.valueOf(this.defaultPrice) + " " + this.defaultCurrency.toString();
    }

    /**
     * Sets the defaultPrice and the defaultCurrency attributes of the Product.
     * @param price float which represents the Product price.
     * @param currency An object of the Currency class.
     */
    public void setPrice(float price, String currency) {
        this.defaultPrice = price;
        this.defaultCurrency = Currency.getInstance(currency);
    }

    /**
     * @return Returns the ProductCategory which the Product belongs to.
     */
    public ProductCategory getProductCategory() {
        return productCategory;
    }

    /**
     * Sets the ProductCategory to the Product. Adds the Product to the ProductCategory list of Products.
     * @param productCategory Instance of the ProductCategory class.
     */
    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
        this.productCategory.addProduct(this);
    }

    /**
     * @return Returns the Product Supplier.
     */
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * Sets the Product supplier to an Instance of the Supplier class.
     * @param supplier Instance of the Supplier class.
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        this.supplier.addProduct(this);
    }

    /**
     * Override the class toString() method to display the Product attributes in a pretty format.
     * @return Returns String
     */
    @Override
    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "defaultPrice: %3$f, " +
                        "defaultCurrency: %4$s, " +
                        "productCategory: %5$s, " +
                        "supplier: %6$s",
                this.id,
                this.name,
                this.defaultPrice,
                this.defaultCurrency.toString(),
                this.productCategory.getName(),
                this.supplier.getName());
    }
}
