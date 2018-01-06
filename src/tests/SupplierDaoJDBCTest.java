import com.codecool.shop.dao.implementation.JDBC.SupplierDaoJDBC;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SupplierDaoJDBCTest extends SupplierDaoTest<SupplierDaoJDBC> {

    @Override
    protected SupplierDaoJDBC createInstance() {
        SupplierDaoJDBC instance = SupplierDaoJDBC.getInstance();
        instance.setFilePath("src/main/resources/sql/config_test.properties");
        return SupplierDaoJDBC.getInstance();
    }

    @Test
    @Override
    public void testFind() {
        instance.add(objectToTest);
        Supplier actual = instance.find(objectToTest.getName());
        assertNotNull(actual);
    }

    @Test
    @Override
    void testAdd() {
        instance.add(objectToTest);
        Supplier expected = objectToTest;
        Supplier actual = instance.find(objectToTest.getName());
        assertEquals(expected.getName(), actual.getName());
    }
}
