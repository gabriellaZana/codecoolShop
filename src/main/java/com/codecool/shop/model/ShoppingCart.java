package com.codecool.shop.model;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shopping cart class, written to be a singleton.
 */
public final class ShoppingCart {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCart.class);

    private static ShoppingCart instance = null;
    private static List<Product> products = new ArrayList<>();
    private ShoppingCart() {
        logger.debug("Shopping cart has been created.");
    }

    /**
     * The method is written with Singleton pattern, creates a ShoppingCart instance if it doesn't exists,
     * returns the existing one otherwise.
     * @return Returns the ShoppingCart instance.
     */
    public static ShoppingCart getInstance() {
        if(instance == null){
            instance = new ShoppingCart();
        }
        logger.debug("Shopping cart already exists.");
        return instance;
    }

    /**
     * Puts the given Product object in the shopping cart, by adding it to the products list.
     * @param product A Product object which needs to be put in the cart.
     */
    public void putProductToCart(Product product){
        products.add(product);
        logger.info("Product - {} - has been added to your cart.", product.name);
        logger.info("Quantity of this product in cart is: {}.", ShoppingCart.getInstance().getQuantity(product));
    }

    /**
     * @return Returns the products list, which are items in the shopping cart.
     */
    public List<Product> getProductsFromCart(){
        return products;
    }


    public Set<Product> getProductsFromCartInSet() {
        Set<Product> products = new HashSet<>();
        List<Product> productList = getProductsFromCart();
        List<String> productNames = new ArrayList<>();
        for (Product product: productList) {
            productNames.add(product.getName());
            if(Collections.frequency(productNames, product.getName()) == 1){
                products.add(product);
            }
        }
        return products;
    }

    /**
     * Iterates through the products in the cart and counting the quantity of the given product.
     * @param product A Product object which needs to be counted.
     * @return Returns the quantity of the given Product in the cart, zero if it's not in it.
     */
    public Integer getQuantity(Product product) {
        Integer quant = 0;
        for (Product prod: getProductsFromCart()) {
            if (prod.id == product.id) {
                quant ++;
            }
        }
        logger.info("Quantity of this product in cart is: {}.", quant);
        return quant;
    }

    public Float getTotalPrice() {
        Float totalPrice = 0f;
        for (Product prod : products) {
            totalPrice += prod.getDefaultPrice();
        }
        return totalPrice;
    }

    /**
     * Deletes item from the shopping cart with the given id by iterating through list of products and removing the item when found.
     * @param id A unique number for identifying the product in the cart (list of products).
     */
    public void deleteItemFromCard(int id){
        logger.debug("Id of the product which the customer'd like to delete is: {}.", id);
        for (int i=0; i<products.size(); i++) {
            if(products.get(i).id == id){
                products.remove(i);
            }
        }
        logger.info("Item has been removed from cart.");
    }

    /**
     * Removes all item from the shopping cart.
     */
    public void removeAllItem(){
        products.clear();
        logger.info("All item has been removed from cart.");
    }
}
