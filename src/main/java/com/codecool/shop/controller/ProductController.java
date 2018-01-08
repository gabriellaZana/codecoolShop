package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductDaoJDBC;

import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.ShoppingCart;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.*;


public class ProductController {

    public static ModelAndView renderProducts(Request req, Response res) {
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        List<ProductCategory> categories = productCategoryDataStore.getAll();
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        ProductDaoJDBC productDaoJDBC = ProductDaoJDBC.getInstance();

        Float sum = 0f;

        for (Product product : shoppingCart.getProductsFromCart()) {
            sum += product.getDefaultPrice();
        }


        Map<String, Object> params = new HashMap<>();
        params.put("productDao", productDaoJDBC);
        params.put("productAmount", shoppingCart.getProductsFromCart().size());
        params.put("Price", sum);
        params.put("categories", categories);
        params.put("productAmount", shoppingCart.getProductsFromCart().size());
        return new ModelAndView(params, "product/index");
    }

    public static String renderShoppingCartMini(Request req, Response res) {
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        ProductDaoJDBC productDaoJdbc = ProductDaoJDBC.getInstance();

        shoppingCart.putProductToCart(productDaoJdbc.find(Integer.parseInt(req.body().substring(1, req.body().length() - 1))));


        Float price = 0f;
        Float quant = 0f;
        for (Product prod : shoppingCart.getProductsFromCart()
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
        List<Product> productList = shoppingCart.getProductsFromCart();
        Set<Product> productSet = new HashSet<>();
        List<Integer> ids = new ArrayList<>();
        for (Product product: productList) {
            ids.add(product.getId());
            if(Collections.frequency(ids, product.getId()) == 1){
                productSet.add(product);
            }
        }

        Map params = new HashMap<>();

        params.put("cart", shoppingCart);
        params.put("products", productSet);
        return new ModelAndView(params, "product/cart");
    }

    public static String deleteItem(Request req, Response res) {
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        shoppingCart.deleteItemFromCard(Integer.parseInt(req.body().substring(1, req.body().length() - 1)));
        return "Deleted";
    }

    public static ModelAndView submitCart(Request req, Response res) {
        System.out.println("submit carrt");
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        shoppingCart.removeAllItem();

        return renderProducts(req, res);
    }
}

