package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.ShoppingCart;
import com.codecool.shop.model.Supplier;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.ModelAndView;

import java.util.*;
import java.util.stream.IntStream;

public class ProductController {

    public static ModelAndView renderProducts(Request req, Response res) {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        List<ProductCategory> categories = productCategoryDataStore.getAll();
        ShoppingCart shoppingCart = ShoppingCart.getInstance();

        Float sum = 0f;

        for (Product prod: shoppingCart.getProductsFromCart()
             ) {
            sum += prod.getDefaultPrice();
        }


        Map params = new HashMap<>();
        params.put("productNumber", shoppingCart.getProductsFromCart().size());
        params.put("Price", sum);
        params.put("categories", categories);
        return new ModelAndView(params, "product/index");
    }

    public static String renderShoppingCartMini(Request req, Response res){
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        ProductDaoMem productDaoMem = ProductDaoMem.getInstance();
        shoppingCart.putProductToCart(productDaoMem.find(Integer.parseInt(req.body().substring(1, req.body().length()-1))));



        Float price = 0f;
        Float quant = 0f;
        for (Product prod: shoppingCart.getProductsFromCart()
             ) {
            price += prod.getDefaultPrice();
            quant++;
        }




        Map<String, Float> sumAndQuantity = new HashMap<>();
        sumAndQuantity.put("quantity", quant);
        sumAndQuantity.put("sum", price);


        Gson gson = new Gson();
        return gson.toJson(sumAndQuantity);
    }


    public static ModelAndView renderShoppingCart(Request req, Response res) {
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        Set<Product> productSet = new HashSet<>(shoppingCart.getProductsFromCart());

        Map params = new HashMap<>();

        params.put("cart", shoppingCart);
        params.put("products", productSet);
        return new ModelAndView(params, "product/cart");
    }


}
