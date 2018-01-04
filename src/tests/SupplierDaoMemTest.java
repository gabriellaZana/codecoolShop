import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;

public class SupplierDaoMemTest extends SupplierDaoTest<SupplierDaoMem> {

    @Override
    protected SupplierDaoMem createInstance() {
        return SupplierDaoMem.getInstance();
    }
}
