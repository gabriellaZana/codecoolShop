package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ShoppingCart {
    private List<String> productsInCart = new ArrayList<>();
    //private List<Map<String, Integer>> productsList = new ArrayList<>();
    private static ShoppingCart instance = null;

    private static List<Float> prices = new ArrayList<>();
    private static List<Product> products = new ArrayList<>();
    private ShoppingCart() {}

    public static ShoppingCart getInstance() {
        if(instance == null){
            instance = new ShoppingCart();
        }
        return instance;
    }

    public List<String> addToCart(String productId){
        productsInCart.add(productId);
        //System.out.println("addtocartProdList" + productsList);
        return productsInCart;
    }


    public Integer getCartSize(){
        return productsInCart.size();
    }

    public List<String> getProductsInCart(){
        return productsInCart;
    }

    public List<Float> addPrice(Float price){
        prices.add(price);
        return prices;
    }

    public List<Float> getPrices(){
        return prices;
    }

    public void putProductToCart(Product product){
        products.add(product);
    }

    public List<Product> getProductsFromCart(){
        return products;
    }

/*    public List<Map<String, Integer>> getProductsList(){
        System.out.println("getproductlist" + productsList);
        return productsList;
    }*/


/*    public Integer getProductsQuantityInCart() {
        Integer sum = 0;
        for(Integer i : productsInCart.values()){
            //System.out.println(i);
            sum += i;
        }
        return sum;
    }*/

    public Float getSumOfCart(){
        return 0f;
    }

    public Integer getQuantity(String productId) {
        Integer quant = 0;
        List<String> products = getProductsInCart();
        for (String prod: products) {
            String subprod = prod.substring(1,prod.length()-1);
            if (subprod.equals(productId)) {
                quant ++;
            }
        }
        return quant;
    }
}
