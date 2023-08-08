package lk.ijse.gdse.pos_backend.dao.custom.impl;

import lk.ijse.gdse.pos_backend.dao.custom.CustomerDAO;
import lk.ijse.gdse.pos_backend.dao.exception.ConstrainViolationException;
import lk.ijse.gdse.pos_backend.dao.util.DBUtil;
import lk.ijse.gdse.pos_backend.entity.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    private Connection connection;

    public CustomerDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        return getList(DBUtil.executeQuery(connection, "select * from customer"));
    }

    @Override
    public Customer save(Customer entity) throws SQLException {
        if (!DBUtil.executeUpdate(connection, "INSERT INTO Customer VALUES(?,?,?,?)", entity.getCustomerID(), entity.getName(), entity.getAddress(), entity.getContact())) {
            throw new ConstrainViolationException("Failed to save customer..");
        }
        return entity;
    }

    @Override
    public Customer update(Customer entity) throws SQLException {
        if (!DBUtil.executeUpdate(connection, "update customer set name=?, address=?, contact=? where customerId=?", entity.getName(), entity.getAddress(), entity.getContact(), entity.getCustomerID())) {
            throw new ConstrainViolationException("Failed to save customer..");
        }
        return entity;
    }

    @Override
    public void delete(String id) throws SQLException {
        if (!DBUtil.executeUpdate(connection, "DELETE FROM Customer WHERE customerId=?", id))
            throw new ConstrainViolationException("Failed to delete customer !");
    }

    private List<Customer> getList(ResultSet resultSet) throws SQLException {

        List<Customer> customerList = new ArrayList<>();

        while (resultSet.next()) {
            customerList.add(new Customer(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)));
        }

        return customerList;
    }
}
