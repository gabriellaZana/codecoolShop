package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Shopping cart class, written to be a singleton.
 */
public final class ShoppingCart {
    private static ShoppingCart instance = null;
    private static List<Product> products = new ArrayList<>();
    private ShoppingCart() {}

    /**
     * The method is written with Singleton pattern, creates a ShoppingCart instance if it doesn't exists,
     * returns the existing one otherwise.
     * @return Returns the ShoppingCart instance.
     */
    public static ShoppingCart getInstance() {
        if(instance == null){
            instance = new ShoppingCart();
        }
        return instance;
    }

    /**
     * Puts the given Product object in the shopping cart, by adding it to the products list.
     * @param product A Product object which needs to be put in the cart.
     */
    public void putProductToCart(Product product){
        products.add(product);
    }

    /**
     * @return Returns the products list, which are items in the shopping cart.
     */
    public List<Product> getProductsFromCart(){
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
        return quant;
    }

    /**
     * Deletes item from the shopping cart with the given id by iterating through list of products and removing the item when found.
     * @param id A unique number for identifying the product in the cart (list of products).
     */
    public void deleteItemFromCard(int id){
        for (int i=0; i<products.size(); i++) {
            if(products.get(i).id == id){
                products.remove(i);
            }
        }
    }

    /**
     * Removes all item from the shopping cart.
     */
    public void removeAllItem(){
        products.clear();
    }
}
