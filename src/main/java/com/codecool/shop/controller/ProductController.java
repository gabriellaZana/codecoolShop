package com.codecool.shop.controller;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.implementation.JDBC.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductDaoJDBC;

import com.codecool.shop.exception.ConnectToStorageFailed;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.ShoppingCart;
import com.codecool.shop.utils.Mailer;
import com.google.gson.Gson;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.*;

/**
 * ProductController
 * <p>
 * <p>Controls the data from the client and the server</p>
 *
 * @author Javengers
 * @version 1.1
 */
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    /**
     * Collects the ProductCategories and the shopping cart content.
     *
     * @param req a Request Object gotten from the client side.
     * @param res a Response Object.
     * @return Returns a ModelAndView with a Map for the thymeleaf template engine.
     */

    public static ModelAndView renderProducts(Request req, Response res) throws ConnectToStorageFailed {


        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        List<ProductCategory> categories = productCategoryDataStore.getAll();
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        ProductDaoJDBC productDaoJDBC = ProductDaoJDBC.getInstance();
        
        Float sum = 0f;

        for (Product product : shoppingCart.getProductsFromCart()) {
            sum += product.getDefaultPrice();
        }
        logger.info("Shopping cart sum: {}", sum);


        Map<String, Object> params = new HashMap<>();
        params.put("productDao", productDaoJDBC);
        params.put("productAmount", shoppingCart.getProductsFromCart().size());
        params.put("Price", sum);
        params.put("categories", categories);

        return new ModelAndView(params, "product/index");
    }

    /**
     * Calculate the data for the ShoppingCart.
     *
     * @param req a Request Object.
     * @param res a Response Object.
     * @return Returns a JSON with the ShoppingCart calculated price and Product quantity.
     */
    public static String renderShoppingCartMini(Request req, Response res) {
        try {
            ShoppingCart shoppingCart = ShoppingCart.getInstance();
            ProductDaoJDBC productDaoJdbc = ProductDaoJDBC.getInstance();
            Map<String, Float> sumAndQuantity = new HashMap<>();
            Gson gson = new Gson();
            Product product = productDaoJdbc.find(Integer.parseInt(req.body().substring(1, req.body().length() - 1)));
            if (product == null) {
                throw new ConnectToStorageFailed("No product with this id");
            }else{
                shoppingCart.putProductToCart(product);
            }


            Float price = 0f;
            Float quant = 0f;
            for (Product prod : shoppingCart.getProductsFromCart()
                    ) {
                price += prod.getDefaultPrice();
                quant++;
            }
            logger.info("Item quantity in shopping cart: {}, total price: {}", quant, price);


            sumAndQuantity.put("quantity", quant);
            sumAndQuantity.put("sum", price);

            logger.debug("", gson.toString());
            return gson.toJson(sumAndQuantity);
        } catch (ConnectToStorageFailed e) {
            res.status(HttpStatus.NOT_FOUND_404);
            return e.getMessage();
        }
    }

    /**
     * Collects the Products for the ShoppingCart view.
     *
     * @param req a Request object from the client.
     * @param res a Response object.
     * @return Returns a ModelAndView with a Map for the thymeleaf template engine.
     */
    public static ModelAndView renderShoppingCart(Request req, Response res) {
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        List<Product> productList = shoppingCart.getProductsFromCart();
        Set<Product> productSet = new HashSet<>();
        List<Integer> ids = new ArrayList<>();
        for (Product product : productList) {
            ids.add(product.getId());
            if (Collections.frequency(ids, product.getId()) == 1) {
                productSet.add(product);
            }
        }

        Map params = new HashMap<>();

        params.put("cart", shoppingCart);
        params.put("products", productSet);
        logger.debug("Total products in shopping cart: {}", productSet.size());
        return new ModelAndView(params, "product/cart");
    }

    /**
     * Deletes an Product from the ShoppingCart.
     *
     * @param req a Request object.
     * @param res a Response object.
     * @return Returns a String.
     */
    public static String deleteItem(Request req, Response res) {
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        shoppingCart.deleteItemFromCard(Integer.parseInt(req.body().substring(1, req.body().length() - 1)));
        logger.debug("Deleted item id: {}", Integer.parseInt(req.body().substring(1, req.body().length() - 1)));
        return "Deleted";
    }

    /**
     * Removes all Product from the ShoppingCart.
     *
     * @param req a Request object.
     * @param res a Response object.
     * @return Returns the renderProducts ModelAndView.
     */

    public static String emptyCart(Request req, Response res) throws ConnectToStorageFailed {
        OrderDaoJDBC orderDaoJDBC = OrderDaoJDBC.getInstance();
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        int orderId = orderDaoJDBC.add(1, shoppingCart);

        shoppingCart.removeAllItem();
        logger.info("Order completed, shopping cart items deleted");
        //Mailer.send("grannyshop.javengers@gmail.com","grannyshop", "nopiwork@gmail.com", "test", "test message");
        Gson gson = new Gson();
        return gson.toJson(orderId);
    }

    public static String getTotalPriceOfCart(Request req, Response res) {
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
//        res.body(shoppingCart.getTotalPrice().toString());
        Gson gson = new Gson();
        return gson.toJson(shoppingCart.getTotalPrice());
    }

    public static ModelAndView renderOrderPage(Request req, Response res) throws ConnectToStorageFailed{
        Integer orderId = Integer.parseInt(req.params(":id"));
        System.out.println(orderId);
        Order order = OrderDaoJDBC.getInstance().find(orderId);
        List<Product> productList = order.getProductList();
        Float price = 0f;
        for (Product prod : productList) {
            price += prod.getDefaultPrice();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("products", productList);
        params.put("finalPrice", price);
        return new ModelAndView(params, "order_details");
    }


}

