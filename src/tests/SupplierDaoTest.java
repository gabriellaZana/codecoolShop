import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class SupplierDaoTest<T extends SupplierDao> {
    private T instance = null;
    protected abstract T createInstance();

    @BeforeEach
    public void setup() {
        instance = createInstance();
    }

    @Test
    public void testAdd() {
        Supplier supp = new Supplier("test_name", "test_description");
        instance.add(supp);
        String expected = supp.getName();
        String actual = instance.find(1).getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testFind() {
        
    }

    @Test
    public void testRemove() {
        fail(" ");
    }

    @Test
    public void testGetAll() {
        fail(" ");
    }


}