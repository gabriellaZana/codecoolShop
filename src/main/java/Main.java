import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.*;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) {

        // default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(8888);

        // populate some data for the memory storage
        populateData();

        // Always start with more specific routes
        get("/hello", (req, res) -> "Hello World");

        // Always add generic routes to the end
        get("/", ProductController::renderProducts, new ThymeleafTemplateEngine());
        // Equivalent with above
        get("/index", (Request req, Response res) -> {
           return new ThymeleafTemplateEngine().render( ProductController.renderProducts(req, res) );
        });

        // Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

    public static void populateData() {

        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();

        //setting up a new supplier
        Supplier Home = new Supplier("Straight from home", "Grannys from home.");
        supplierDataStore.add(Home);
        Supplier Countryside = new Supplier("Countryside", "Grannys from the countryside");
        supplierDataStore.add(Countryside);
        Supplier City = new Supplier("City", "Grannys from the city");
        supplierDataStore.add(City);
        Supplier NursingHome = new Supplier("Nursing home", "Grannys fresh from the nursing homes.");
        supplierDataStore.add(NursingHome);

        //setting up a new product category
        ProductCategory Granny = new ProductCategory("Granny", "", "Grannys for rent.");
        productCategoryDataStore.add(Granny);
        ProductCategory accessoriesForCooking = new ProductCategory("Accesories1", "Misc1", "All what grannys' need1");
        productCategoryDataStore.add(accessoriesForCooking);
        ProductCategory accessories2 = new ProductCategory("Accesories2", "Misc2", "All what grannys' need2");
        productCategoryDataStore.add(accessories2);
        ProductCategory accessories3 = new ProductCategory("Accesories3", "Misc3", "All what grannys' need3");
        productCategoryDataStore.add(accessories3);
        ProductCategory accessories4 = new ProductCategory("Accesories4", "Misc4", "All what grannys' need4");
        productCategoryDataStore.add(accessories4);


        //setting up products and printing it
        productDataStore.add(new Product("Rebel granny", 49.9f, "USD", "Likes metal. Perfect choice for punks and metalheads.", Granny, Home));
        productDataStore.add(new Product("Rich granny", 479, "USD", "Classic style, royal apperance, two guards included.", Granny, City));
        productDataStore.add(new Product("Christmas edition granny", 89, "USD", "Singing christmas melodies all the time, smells a bit like eggnog.", Granny, NursingHome));
        productDataStore.add(new Product("Cyber granny", 5, "EUR","Exceptional reading skills, digital nomad.", Granny, City));
        productDataStore.add(new Product("Muriel", 5, "EUR","Pet-friendly, has a cowardly dog.", Granny, Countryside));
    }


}
