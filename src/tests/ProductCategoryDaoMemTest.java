import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;

public class ProductCategoryDaoMemTest extends ProductCategoryDaoTest<ProductCategoryDaoMem> {

    @Override
    protected ProductCategoryDaoMem createInstance() {
        return ProductCategoryDaoMem.getInstance();
    }
}