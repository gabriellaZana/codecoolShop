import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class SupplierDaoTest<T extends SupplierDao> {
    T instance = null;
    static Supplier objectToTest = null;
    protected abstract T createInstance();

    @BeforeAll
    static void beforeAll() {
        objectToTest = new Supplier("1st_test_supplier", "1st_test_supplier_desc");
        objectToTest.setId(0);
    }

    @BeforeEach
    void setup() {
        instance = createInstance();
    }

    @AfterEach
    void cleanup() {
        instance.remove(objectToTest.getId());
    }

    @Test
    void testAdd() {
        instance.add(objectToTest);
        Supplier expected = objectToTest;
        Supplier actual = instance.find(objectToTest.getId());
        assertEquals(expected, actual);
    }

    @Test
    void testFind() {
        instance.add(objectToTest);
        Supplier actual = instance.find(objectToTest.getId());
        assertNotNull(actual);
    }

    @Test
    void testRemove() {
        instance.add(objectToTest);
        instance.remove(objectToTest.getId());
        Supplier actual = instance.find(objectToTest.getId());
        assertNull(actual);
    }

    @Test
    void testGetAll() {
        instance.add(objectToTest);
        List<Supplier> expected = new ArrayList<>(Arrays.asList(objectToTest));
        List<Supplier> actual = instance.getAll();
        assertEquals(expected.size(), actual.size());
    }
}
