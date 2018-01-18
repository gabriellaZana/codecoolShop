package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.exception.ConnectToStorageFailed;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCart;
import com.codecool.shop.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class OrderDaoJDBC implements OrderDao {

    private static OrderDaoJDBC instance = null;

    private String filePath = "src/main/resources/sql/connection.properties";
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance(this.filePath);

    private OrderDaoJDBC() {
    }

    public static OrderDaoJDBC getInstance() {
        if (instance == null) {
            instance = new OrderDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(int userId, ShoppingCart shoppingCart) throws ConnectToStorageFailed {
        ProductDaoJDBC productDaoJDBC = ProductDaoJDBC.getInstance();
        String orderQuery = "INSERT INTO orders (user_id, order_time, status) VALUES (?, CURRENT_TIMESTAMP, 'new') RETURNING *;";
        String productOrderQuery = "INSERT INTO products_of_order (order_id, product_id, quantity) VALUES (?, ?, ?);";
        Set<Product> products = shoppingCart.getProductsFromCartInSet();
        try (Connection connection = databaseConnection.getConnection()) {
            PreparedStatement preparedOrderStatement = connection.prepareStatement(orderQuery);
            preparedOrderStatement.setInt(1, userId);
            ResultSet resultSet = preparedOrderStatement.executeQuery();
            if (resultSet.next()) {
                int orderId = resultSet.getInt("id");
                for (Product product : products) {
                    Connection loopConnection = databaseConnection.getConnection();
                    PreparedStatement preparedStatement = loopConnection.prepareStatement(productOrderQuery);
                    preparedStatement.setInt(1, orderId);
                    preparedStatement.setInt(2, productDaoJDBC.find(product.getName()).getId());
                    preparedStatement.setInt(3, shoppingCart.getQuantity(product));
                    preparedStatement.execute();
                    loopConnection.close();
                }
            }
        }
        catch (SQLException e){
            throw new ConnectToStorageFailed(e.getMessage());
        }
    }

    @Override
    public Order find(int orderId) throws ConnectToStorageFailed {
        ProductDaoJDBC productDaoJDBC = ProductDaoJDBC.getInstance();
        String findOrderQuery = "SELECT * FROM orders WHERE id = ?;";
        String findProductsOfOrderQuery = "SELECT * FROM products_of_order WHERE order_id = ?;";
        List<Product> products = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection()) {
            PreparedStatement preparedOrderStatement = connection.prepareStatement(findOrderQuery);
            preparedOrderStatement.setInt(1, orderId);
            ResultSet resultSet = preparedOrderStatement.executeQuery();
            if(resultSet.next()){
                int userId = resultSet.getInt("user_id");
                PreparedStatement preparedStatement = connection.prepareStatement(findProductsOfOrderQuery);
                preparedStatement.setInt(1, resultSet.getInt("id"));
                ResultSet orderResultSet = preparedStatement.executeQuery();
                while (orderResultSet.next()){
                    Product product = productDaoJDBC.find(orderResultSet.getInt("product_id"));
                    products.add(product);
                    for (int i = 1; i < orderResultSet.getInt("quantity"); i++) {
                        products.add(product);
                    }
                }
                return new Order(userId, products);
            }
        }catch (SQLException e){
            throw new ConnectToStorageFailed(e.getMessage());
        }
        return null;
    }
}
