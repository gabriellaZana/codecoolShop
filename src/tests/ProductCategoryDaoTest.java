import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ProductCategoryDaoTest<T extends ProductCategoryDao> {

    T instance = null;
    static ProductCategory objectToTest = null;
    protected abstract T createInstance();

    @BeforeAll
    static void beforeAll() {
        objectToTest = new ProductCategory("1st_test_category", "1st_test_category_desc");
        objectToTest.setId(1);
    }

    @BeforeEach
    void setup() {
        instance = createInstance();
    }

    @Test
    void testAdd() {
        instance.add(objectToTest);
        ProductCategory expected = objectToTest;
        ProductCategory actual = instance.find(objectToTest.getId());
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void testFind() {
        ProductCategory actual = instance.find(objectToTest.getId());
        assertNotNull(actual);
    }

    @Test
    void testRemove() {
        instance.add(objectToTest);
        instance.remove(objectToTest.getId());
        ProductCategory actual = instance.find(objectToTest.getId());
        assertNull(actual);
    }

    @Test
    void testGetAll() {
        List<ProductCategory> expected = new ArrayList<>(Arrays.asList(objectToTest));
        List<ProductCategory> actual = instance.getAll();
        assertEquals(expected.toString(), actual.toString());
    }
}