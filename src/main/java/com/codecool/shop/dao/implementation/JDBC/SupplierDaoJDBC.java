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
    private PreparedStatement statement = null;
    private String filePath = "src/main/resources/sql/connection.properties";

    private SupplierDaoJDBC() {
    }

    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;

    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void add(Supplier supplier) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);
        Connection connection = databaseConnection.getConnection();
        if (find(supplier.getName()) != null) {
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
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);
        Connection connection = databaseConnection.getConnection();
        Supplier resultSupplier = null;
        try {
            String getProductQuery = "SELECT * FROM suppliers WHERE id=?;";
            statement = connection.prepareStatement(getProductQuery);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                resultSupplier = new Supplier(result.getString("name"), result.getString("description"));
                resultSupplier.setId(result.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSupplier;
    }


    public Supplier find(String name) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);
        Connection connection = databaseConnection.getConnection();
        Supplier resultSupplier = null;
        try {
            String getProductQuery = "SELECT * FROM suppliers WHERE name=?;";
            statement = connection.prepareStatement(getProductQuery);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                resultSupplier = new Supplier(result.getString("name"), result.getString("description"));
                resultSupplier.setId(result.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSupplier;
    }

    @Override
    public void remove(int id) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);
        Connection connection = databaseConnection.getConnection();
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
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);
        Connection connection = databaseConnection.getConnection();
        List<Supplier> supplierList = new ArrayList<>();
        try {
            String getSuppliersQuery = "SELECT * FROM suppliers;";
            statement = connection.prepareStatement(getSuppliersQuery);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Supplier supplier = new Supplier(result.getString("name"), result.getString("description"));
                supplierList.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplierList;
    }
}
