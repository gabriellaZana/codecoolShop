import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.SupplierDaoJDBC;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductDaoJDBCTest {
    ProductDaoJDBC productDaoJDBC = ProductDaoJDBC.getInstance();
    SupplierDaoJDBC supplierDaoJDBC = SupplierDaoJDBC.getInstance();
    ProductCategoryDaoJDBC productCategoryDaoJDBC = ProductCategoryDaoJDBC.getInstance();

    ProductCategory testProductCategory = new ProductCategory("testCategory", "testDescription");
    Supplier testSupplier = new Supplier("testSupplier", "testDescription");
    Product product = new Product("testName", 999, "USD", "testDesciption", testProductCategory, testSupplier );


    @BeforeEach
    void setFilePath(){
        productCategoryDaoJDBC.setFilePath("src/main/resources/sql/config_test.properties");
        supplierDaoJDBC.setFilePath("src/main/resources/sql/config_test.properties");
        productDaoJDBC.setFilePath("src/main/resources/sql/config_test.properties");
    }

    void populateDb(){
        productCategoryDaoJDBC.add(testProductCategory);
        supplierDaoJDBC.add(testSupplier);
        productDaoJDBC.add(product);

    }

    @Test
    void testAddTwoProducts(){
        populateDb();
        Product product2 = new Product("testName2", 999, "USD", "testDesciption", testProductCategory, testSupplier );
        productDaoJDBC.add(product2);
        assertTrue(productDaoJDBC.getAll().size() == 3);
    }


    @Test
    void testFind(){
        Product testProduct = productDaoJDBC.find(3);
        assertTrue(product.getName().equals("testName"));
    }

    @Test
    void testRemove(){
        Product product3 = new Product("testName3", 999, "USD", "testDesciption", testProductCategory, testSupplier );
        productDaoJDBC.add(product3);
        productDaoJDBC.remove(3);
        assertTrue(productDaoJDBC.getAll().size() == 3);
    }

    @Test
    void testGetAll(){
        assertTrue(productDaoJDBC.getAll().size() == 3);
    }

    @Test
    void testGetBySupplier(){
        String testProduct = productDaoJDBC.getBy(testSupplier).get(0).getName();
        assertEquals(testProduct, product.getName());
    }

    @Test
    void testGetByCategory(){
        assertEquals(productDaoJDBC.getBy(testProductCategory).get(0), product);
    }
}

