import com.codecool.shop.dao.implementation.JDBC.SupplierDaoJDBC;

public class SupplierDaoJDBCTest extends SupplierDaoTest<SupplierDaoJDBC> {

    @Override
    protected SupplierDaoJDBC createInstance() {
        SupplierDaoJDBC instance = SupplierDaoJDBC.getInstance();
        instance.setFilePath("src/main/resources/sql/config_test.properties");
        return SupplierDaoJDBC.getInstance();
    }
}
