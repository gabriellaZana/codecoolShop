package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SupplierDaoJDBC implements SupplierDao {
    private static SupplierDaoJDBC instance = null;
    private String filePath = "src/main/resources/sql/connection.properties";
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);

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
    public void add(Supplier supplier) throws SQLException {
        if (find(supplier.getName()) != null) {
            return;
        }
        String addQuery = "INSERT INTO suppliers (name, description) VALUES (?, ?);";
        ArrayList<Object> infos = new ArrayList<>(Arrays.asList(supplier.getName(), supplier.getDescription()));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, addQuery)) {
            statement.executeUpdate();
        }
    }


    @Override
    public Supplier find(int id) throws SQLException {
        String getProductQuery = "SELECT * FROM suppliers WHERE id=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        return executeFindQuery(getProductQuery, infos);
    }


    public Supplier find(String name) throws SQLException {
        String getProductQuery = "SELECT * FROM suppliers WHERE name=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(name));
        return executeFindQuery(getProductQuery, infos);

    }

    private Supplier executeFindQuery(String query, ArrayList<Object> infos) throws SQLException {
        Supplier resultSupplier = null;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                resultSupplier = new Supplier(result.getString("name"), result.getString("description"));
                resultSupplier.setId(result.getInt("id"));
            }
        }
        return resultSupplier;
    }

    @Override
    public void remove(int id) throws SQLException {
        String removeSupplierQuery = "DELETE FROM suppliers WHERE id=?;";
        ArrayList<Object> infos = new ArrayList<>(Collections.singletonList(id));
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = databaseConnection.createAndSetPreparedStatement(connection, infos, removeSupplierQuery)) {
            statement.executeUpdate();
        }
    }

    @Override
    public List<Supplier> getAll() throws SQLException {
        String getSuppliersQuery = "SELECT * FROM suppliers;";
        List<Supplier> supplierList = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(getSuppliersQuery);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Supplier supplier = new Supplier(result.getString("name"), result.getString("description"));
                supplier.setId(result.getInt("id"));
                supplierList.add(supplier);
            }
        }
        return supplierList;
    }
}
