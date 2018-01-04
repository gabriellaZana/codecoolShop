import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class SupplierDaoTest<T> {
    private T instance;
    protected abstract T createInstance();

    @BeforeEach
    public void setup() {
        instance = createInstance();
    }

    @Test
    public void testAdd() {
    }

    @Test
    public void testFind() {}

    @Test
    public void testRemove() {}

    @Test
    public void testGetAll() {}


}
