import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.controller.UserController;
import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.SupplierDaoJDBC;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.model.*;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import javax.xml.ws.ResponseWrapper;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        // default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(8888);

        // populate some data for the memory storage
        try {
            populateData();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Always start with more specific routes
        get("/hello", (req, res) -> "Hello World");

        // Always add generic routes to the end
        get("/", (Request request, Response response) -> {
                    try {
                        return new ThymeleafTemplateEngine().render(ProductController.renderProducts(request, response));
                    } catch (Exception e) {
                        response.status(HttpStatus.SERVICE_UNAVAILABLE_503);
                        return "<html><body><h1>" + response.raw().getStatus() + "</h1><p>SERVICE UNAVAILABLE</p></body></html>";
                    }

                }
        );
        // Equivalent with above
        get("/index", (Request request, Response response) -> {
            try {
                return new ThymeleafTemplateEngine().render(ProductController.renderProducts(request, response));
            } catch (Exception e) {
                response.status(HttpStatus.SERVICE_UNAVAILABLE_503);
                return "<html><body><h1>" + response.raw().getStatus() + "</h1><p>SERVICE UNAVAILABLE</p></body></html>";
            }

        });

        post("/shopping-cart", ProductController::renderShoppingCartMini);

        get("/cart", (Request request, Response response) -> {
            try {
                return new ThymeleafTemplateEngine().render(ProductController.renderShoppingCart(request, response));
            } catch (Exception e) {
                response.status(HttpStatus.SERVICE_UNAVAILABLE_503);
                return "<html><body><h1>" + response.raw().getStatus() + "</h1><p>SERVICE UNAVAILABLE</p></body></html>";
            }

        });

        post("/delete-item", ProductController::deleteItem);

        post("/submit-cart", ProductController::submitCart);

        post("/register", UserController::register);

        post("/login", UserController::login);

        get("/logout", UserController::logout);

        // Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

    public static void populateData() throws SQLException {

        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
        SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();


        //setting up a new supplier
        Supplier Home = new Supplier("Straight from home", "Grannys from home.");
        supplierDataStore.add(Home);
        Supplier Countryside = new Supplier("Countryside", "Grannys from the countryside");
        supplierDataStore.add(Countryside);
        Supplier City = new Supplier("City", "Grannys from the city");
        supplierDataStore.add(City);
        Supplier NursingHome = new Supplier("Nursing home", "Grannys fresh from the nursing homes.");
        supplierDataStore.add(NursingHome);
        Supplier Amazon = new Supplier("The Amazon", "Everything you ever want in life.");
        supplierDataStore.add(Amazon);

        //setting up a new product category
        ProductCategory Granny = new ProductCategory("Grannys", "Grannys for rent.");
        productCategoryDataStore.add(Granny);
        ProductCategory accessoriesForCooking = new ProductCategory("Accessories for cooking", "All what grannys' need1");
        productCategoryDataStore.add(accessoriesForCooking);

        //setting up products and printing it
        productDataStore.add(new Product("Rebel granny", 666, "USD", "Likes metal. Perfect choice for punks and metalheads.", Granny, Home));
        productDataStore.add(new Product("Rich granny", 9999, "USD", "Classic style, royal apperance, two guards included.", Granny, City));
        productDataStore.add(new Product("Christmas edition granny", 0.99f, "USD", "Singing christmas melodies all the time, smells a bit like eggnog.", Granny, NursingHome));
        productDataStore.add(new Product("Cyber granny", 10101, "USD", "Exceptional reading skills, digital nomad.", Granny, City));
        productDataStore.add(new Product("Muriel", 49.99f, "USD", "Pet-friendly, has a cowardly dog.", Granny, Countryside));
        productDataStore.add(new Product("Assassin grandma", 1499, "USD", "Works silent and fast. Cleans the site after getting the job done.", Granny, Countryside));
        productDataStore.add(new Product("Wooden spoon", 5, "USD", "Good for cooking or educational purposes.", accessoriesForCooking, Amazon));
        productDataStore.add(new Product("Oven gloves", 4.99f, "USD", "If you don't want to burn your hand down.", accessoriesForCooking, Amazon));
        productDataStore.add(new Product("Spotted/dotted pot kit", 15, "USD", "For a good stew!", accessoriesForCooking, Amazon));
        productDataStore.add(new Product("Cook book", 42.0f, "USD", "Special cook book for special grannys.", accessoriesForCooking, Amazon));
    }


}
