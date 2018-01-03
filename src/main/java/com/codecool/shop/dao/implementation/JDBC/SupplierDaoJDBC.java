package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJDBC implements SupplierDao {
    private static SupplierDaoJDBC instance = null;
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private Connection connection = databaseConnection.getConnection();
    private PreparedStatement statement = null;

    private SupplierDaoJDBC() {
    }

    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;

    }

    @Override
    public void add(Supplier supplier) {
        if (find(supplier.getId()) != null) {
            return;
        }
        try {
            String addQuery = "INSERT INTO suppliers (name, description) VALUES (?, ?);";
            statement = connection.prepareStatement(addQuery);
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Supplier find(int id) {
        Supplier resultSupplier = null;
        try {
            String getProductQuery = "SELECT * FROM suppliers WHERE id=?;";
            statement = connection.prepareStatement(getProductQuery);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                resultSupplier = new Supplier(result.getString("name"), result.getString("description"));
            }
        } catch(SQLException e){
                e.printStackTrace();
            }
            return resultSupplier;
        }


    @Override
    public void remove(int id) {
        try {
            String removeSupplierQuery = "DELETE FROM products WHERE id=?;";
            statement = connection.prepareStatement(removeSupplierQuery);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Supplier> getAll() {
        List<Supplier> supplierList = new ArrayList<>();
        try {
            String getSuppliersQuery = "SELECT * FROM suppliers;";
            statement = connection.prepareStatement(getSuppliersQuery);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Supplier supplier =  new Supplier(result.getString("name"), result.getString("description"));
                supplierList.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplierList;
    }
}
