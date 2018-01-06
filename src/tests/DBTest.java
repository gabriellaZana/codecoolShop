import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;
import com.codecool.shop.model.ProductCategory;

public class DBTest {

    public static void main(String[] args) {
        populateTestDB();
    }

    public static void populateTestDB(){
        ProductCategoryDaoJDBC productCategoryDaoJDBC = ProductCategoryDaoJDBC.getInstance();
        productCategoryDaoJDBC.setFilePath("src/main/resources/sql/config_test.properties");
        productCategoryDaoJDBC.add(new ProductCategory("test", "test"));
    }
}
