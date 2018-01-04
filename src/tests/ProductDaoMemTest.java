import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductDaoMemTest  {
    ProductDaoMem productDaoMem = ProductDaoMem.getInstance();
    SupplierDaoMem supplierDaoMem = SupplierDaoMem.getInstance();
    ProductCategoryDaoMem productCategoryDaoMem = ProductCategoryDaoMem.getInstance();

    ProductCategory testProductCategory;
    Supplier testSupplier;
    Product product;


    @BeforeEach
    void clearMem(){
        productDaoMem.getAll().clear();
        supplierDaoMem.getAll().clear();
        productDaoMem.getAll().clear();
        testProductCategory = new ProductCategory("testCategory", "testDescription");
        testSupplier = new Supplier("testSupplier", "testDescription");
        product = new Product("testName", 999, "USD", "testDesciption", testProductCategory, testSupplier );
        productDaoMem.add(product);
    }


    @Test
    void testAddTwoProducts(){

        Product product2 = new Product("testName2", 999, "USD", "testDesciption", testProductCategory, testSupplier );
        productDaoMem.add(product2);
        assertTrue(productDaoMem.getAll().size() == 2);
    }


    @Test
    void testFind(){
        Product product = productDaoMem.find(1);
        assertTrue(product.getName().equals("testName"));
    }

    @Test
    void testRemove(){
        productDaoMem.remove(1);
        assertTrue(productDaoMem.getAll().size() == 0);
    }

    @Test
    void testGetAll(){
        assertTrue(productDaoMem.getAll().size() == 1);
    }

    @Test
    void testGetBySupplier(){
        assertEquals(productDaoMem.getBy(testSupplier).get(0), product);
    }

    @Test
    void testGetByCategory(){
        assertEquals(productDaoMem.getBy(testProductCategory).get(0), product);
    }


}
