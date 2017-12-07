package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

public final class ShoppingCart {
    private static ShoppingCart instance = null;
    private static List<Product> products = new ArrayList<>();
    private ShoppingCart() {}

    public static ShoppingCart getInstance() {
        if(instance == null){
            instance = new ShoppingCart();
        }
        return instance;
    }


    public void putProductToCart(Product product){
        System.out.println("put prod to cart" + product.name);
        products.add(product);
    }

    public List<Product> getProductsFromCart(){
        System.out.println(products);
        System.out.println(products.size());
        return products;
    }


    public Integer getQuantity(Product product) {
        Integer quant = 0;
        for (Product prod: getProductsFromCart()) {
            if (prod.id == product.id) {
                quant ++;
            }
        }
        return quant;
    }

    public void deleteItemFromCard(int id){
        for (int i=0; i<products.size(); i++) {
            if(products.get(i).id == id){
                products.remove(i);
            }
        }
    }
}
