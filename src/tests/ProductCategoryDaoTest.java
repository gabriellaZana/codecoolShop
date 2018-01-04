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

    private T instance = null;
    protected abstract T createInstance();

    @BeforeEach
    public void setup() {
        instance = createInstance();
    }

    @Test
    public void testAdd() {
        ProductCategory cat = new ProductCategory("test_product", "test_product_description");
        instance.add(cat);
        String expected = cat.getName();
        String actual = instance.find(1).getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testFind() {
        ProductCategory cat = new ProductCategory("another_test_product", "another_test_product_desc");
        instance.add(cat);
        String expected = cat.getDescription();
        String actual = instance.find(2).getDescription();
        assertEquals(expected, actual);
    }

    @Test
    public void testRemove() {
        instance.remove(2);
        ProductCategory actual = instance.find(2);
        System.out.println(actual);
        assertNull(actual);
    }

    @Test
    public void testGetAll() {
        List<ProductCategory> expected = new ArrayList<>(Arrays.asList(instance.find(1)));
        List<ProductCategory> actual = instance.getAll();
        assertEquals(expected.toString(), actual.toString());
    }
}