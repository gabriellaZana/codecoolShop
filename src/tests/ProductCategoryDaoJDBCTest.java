import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;

public class ProductCategoryDaoJDBCTest extends ProductCategoryDaoTest<ProductCategoryDaoJDBC> {

    @Override
    protected ProductCategoryDaoJDBC createInstance() {
        ProductCategoryDaoJDBC instance = ProductCategoryDaoJDBC.getInstance();
        instance.setFilePath("src/main/resources/sql/config_test.properties");
        return ProductCategoryDaoJDBC.getInstance();
    }
}
