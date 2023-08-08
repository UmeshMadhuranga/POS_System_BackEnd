package lk.ijse.gdse.pos_backend.dao.custom.impl;

import lk.ijse.gdse.pos_backend.dao.custom.OrderDetailsDAO;
import lk.ijse.gdse.pos_backend.dao.exception.ConstrainViolationException;
import lk.ijse.gdse.pos_backend.dao.util.DBUtil;
import lk.ijse.gdse.pos_backend.entity.OrderDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {

    private Connection connection;

    public OrderDetailsDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<OrderDetails> getAll() throws SQLException {

        return getList(DBUtil.executeQuery(connection, "SELECT*FROM orderDetails"));

    }

    @Override
    public OrderDetails save(OrderDetails entity) throws SQLException {

        if (!DBUtil.executeUpdate(connection, "INSERT INTO orderDetails VALUES(?,?,?,?,?,?,?)",
                entity.getOrderId(), entity.getCustomerId(),entity.getCode(),entity.getName(),entity.getPrice(),
                entity.getQty(),entity.getTotal())) {

            throw new ConstrainViolationException("Failed to save order details..");

        }

        return entity;

    }

    @Override
    public OrderDetails update(OrderDetails entity) throws SQLException {
        return null;
    }

    @Override
    public void delete(String pk) throws SQLException {

    }

    private List<OrderDetails> getList(ResultSet resultSet) throws SQLException {

        List<OrderDetails> orderDetailsList = new ArrayList<>();

        while (resultSet.next()) {

            orderDetailsList.add(new OrderDetails(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getInt(6),
                    resultSet.getDouble(7)
            ));
        }

        return orderDetailsList;
    }
}
