import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductCategoryDaoJDBCTest extends ProductCategoryDaoTest<ProductCategoryDaoJDBC> {

    @Override
    protected ProductCategoryDaoJDBC createInstance() {
        ProductCategoryDaoJDBC instance = ProductCategoryDaoJDBC.getInstance();
        instance.setFilePath("src/main/resources/sql/config_test.properties");
        return ProductCategoryDaoJDBC.getInstance();
    }

    @Test
    @Override
    public void testFind() {
        instance.add(objectToTest);
        ProductCategory actual = instance.find(objectToTest.getName());
        assertNotNull(actual);
    }

    @Test
    @Override
    void testAdd() {
        instance.add(objectToTest);
        ProductCategory expected = objectToTest;
        ProductCategory actual = instance.find(objectToTest.getName());
        assertEquals(expected.getName(), actual.getName());
    }
}
