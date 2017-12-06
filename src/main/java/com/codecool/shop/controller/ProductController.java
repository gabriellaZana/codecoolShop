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

public class ProductController {

    public static ModelAndView renderProducts(Request req, Response res) {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        List<ProductCategory> categories = productCategoryDataStore.getAll();

        Map params = new HashMap<>();

        params.put("categories", categories);
        return new ModelAndView(params, "product/index");
    }

    public static String renderShoppingCart(Request req, Response res){
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        System.out.println(shoppingCart.addToCart(req.body()).toString());


        String productId = req.body().substring(8, req.body().length() - 1);

        Integer productIdInt = Integer.parseInt(productId);

        ProductDaoMem productDaoMem = ProductDaoMem.getInstance();
        Product addedProduct = productDaoMem.find(productIdInt);

        System.out.println(addedProduct.getSupplier());

        List<Product> products = new ArrayList<>();


        Integer quantity = shoppingCart.getProductsQuantityInCart();

        products.add(addedProduct);

        //System.out.println(quantityList.get(0));

        Map params = new HashMap<>();

        params.put("products", products);
        //params.put("price", addedProduct.getPrice());

        Map<String, String> quantityAndPrice = new HashMap<>();
        quantityAndPrice.put("price", addedProduct.getPrice());
        quantityAndPrice.put("quantity", quantity.toString());

        Gson gson = new Gson();
        return gson.toJson(quantityAndPrice);
        //return new ModelAndView(params, "product/index" );
    }

}
