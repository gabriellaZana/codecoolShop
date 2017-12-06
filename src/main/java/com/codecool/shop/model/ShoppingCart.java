package com.codecool.shop.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class ShoppingCart {
    private Map<String, Integer> productsInCart = new HashMap<>();

    private static ShoppingCart instance = null;

    private ShoppingCart() {}
    private static Integer quantity = 0;

    public static ShoppingCart getInstance() {
        if(instance == null){
            instance = new ShoppingCart();
        }
        return instance;
    }

    public Map<String, Integer> addToCart(String productId){
        if(productsInCart.containsKey(productId)){
            Integer productQuantity = productsInCart.get(productId);
            productsInCart.put(productId, productQuantity+1);
        } else{
            productsInCart.put(productId, 1);
        }
        return productsInCart;
    }

    public Integer getProductsQuantityInCart() {
        Iterator iterator = productsInCart.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            Integer value = (Integer) pair.getValue();
            quantity += value;
        }
        return quantity;
    }
}
