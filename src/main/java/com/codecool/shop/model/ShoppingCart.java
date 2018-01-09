package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class ShoppingCart {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCart.class);

    private static ShoppingCart instance = null;
    private static List<Product> products = new ArrayList<>();
    private ShoppingCart() {
        logger.debug("Shopping cart has been created.");
    }

    public static ShoppingCart getInstance() {
        if(instance == null){
            instance = new ShoppingCart();
        }
        logger.debug("Shopping cart already exists.");
        return instance;
    }


    public void putProductToCart(Product product){
        products.add(product);
        logger.info("Product - {} - has been added to your cart.", product.name);
        logger.info("Quantity of this product in cart is: {}.", ShoppingCart.getInstance().getQuantity(product));
    }

    public List<Product> getProductsFromCart(){
        return products;
    }


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

    public void deleteItemFromCard(int id){
        logger.debug("Id of the product which the customer'd like to delete is: {}.", id);
        for (int i=0; i<products.size(); i++) {
            if(products.get(i).id == id){
                products.remove(i);
            }
        }
        logger.info("Item has been removed from cart.");
    }

    public void removeAllItem(){
        products.clear();
        logger.info("All item has been removed from cart.");
    }
}
