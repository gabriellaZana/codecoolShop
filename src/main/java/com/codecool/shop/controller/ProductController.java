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

        Float price = 0f;

        for (Product product: shoppingCart.getProductsFromCart()
             ) {
            price += product.getDefaultPrice();

        }

        Map params = new HashMap<>();
        params.put("productNumber", shoppingCart.getCartSize());
        params.put("Price", price);
        params.put("categories", categories);
        return new ModelAndView(params, "product/index");
    }

    public static String renderShoppingCartMini(Request req, Response res){
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        shoppingCart.addToCart(req.body());
        List<Float> prices = new ArrayList<>();
        List<String> productList = shoppingCart.getProductsInCart();

        for (String prod: productList
                ) {
            String productId = prod.substring(8, prod.length() - 1);
            Integer productIdInt = Integer.parseInt(productId);
            ProductDaoMem productDaoMem = ProductDaoMem.getInstance();
            Product addedProduct = productDaoMem.find(productIdInt);
            prices.add(addedProduct.getDefaultPrice());
            shoppingCart.putProductToCart(addedProduct);
        }



        Integer quantity = shoppingCart.getCartSize();
        Float quant = quantity.floatValue();
        Map params = new HashMap<>();
        Float sum = 0f;
        for (Float price: prices
                ) {sum += price;
        }


        Map<String, Float> sumAndQuantity = new HashMap<>();
        sumAndQuantity.put("quantity", quant);
        sumAndQuantity.put("sum", sum);

        params.put("sumandquantity", sumAndQuantity);

        Gson gson = new Gson();
        return gson.toJson(sumAndQuantity);
    }


    public static ModelAndView renderShoppingCart(Request req, Response res) {
        ShoppingCart shoppingCart = ShoppingCart.getInstance();

        List<Product> products = new ArrayList<>();

        List<String> productList = shoppingCart.getProductsInCart();
        for (String prod: productList
                ) {
            String productId = prod.substring(8, prod.length() - 1);
            Integer productIdInt = Integer.parseInt(productId);
            ProductDaoMem productDaoMem = ProductDaoMem.getInstance();
            Product addedProduct = productDaoMem.find(productIdInt);
            products.add(addedProduct);

        }
        Set<Product> setProduct = new HashSet<>(products);

        Map params = new HashMap<>();
        params.put("cart", shoppingCart);

        params.put("products", setProduct);
        return new ModelAndView(params, "product/cart");
    }


}
