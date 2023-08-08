package lk.ijse.gdse.pos_backend.dao.custom.impl;

import lk.ijse.gdse.pos_backend.dao.custom.ItemDAO;
import lk.ijse.gdse.pos_backend.dao.exception.ConstrainViolationException;
import lk.ijse.gdse.pos_backend.dao.util.DBUtil;
import lk.ijse.gdse.pos_backend.dto.ItemDTO;
import lk.ijse.gdse.pos_backend.entity.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    private Connection connection;

    public ItemDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Item> getAll() throws SQLException {

        return getList(DBUtil.executeQuery(connection, "SELECT * FROM item"));

    }

    @Override
    public Item save(Item entity) throws SQLException {

        if (!DBUtil.executeUpdate(connection, "INSERT INTO item VALUES(?,?,?,?)", entity.getCode(),
                entity.getName(), entity.getPrice(), entity.getQty())) {
            throw new ConstrainViolationException("Failed to save item !");
        }

        return entity;

    }

    @Override
    public Item update(Item entity) throws SQLException {

        if (!DBUtil.executeUpdate(connection, "UPDATE item SET name=?, price=?, qty=? WHERE code=?",
                entity.getName(), entity.getPrice(), entity.getQty(), entity.getCode())) {
            throw new ConstrainViolationException("Failed to update item !");
        }

        return entity;

    }

    @Override
    public boolean updateQty(String code, int qty) throws SQLException {

        boolean isUpdated = false;

        if (!DBUtil.executeUpdate(connection, "UPDATE item set qty=? WHERE code=?", qty, code)) {
            isUpdated = false;
            throw new ConstrainViolationException("Failed to update qty..");
        }
        isUpdated = true;

        return isUpdated;
    }

    @Override
    public ItemDTO getItem(String code) throws SQLException {

        ResultSet resultSet = DBUtil.executeQuery(connection, "select * from item where code=?", code);

        ItemDTO itemDTO = new ItemDTO();

        while (resultSet.next()) {
            itemDTO = new ItemDTO(resultSet.getString(1),resultSet.getString(2),
                    resultSet.getDouble(3), resultSet.getInt(4));
        }

        return itemDTO;
    }

    @Override
    public void delete(String code) throws SQLException {

        if (!DBUtil.executeUpdate(connection, "DELETE FROM item WHERE code=?", code)) {
            throw new ConstrainViolationException("Failed to delete item !");
        }

    }

    private List<Item> getList(ResultSet resultSet) throws SQLException {

        List<Item> itemList = new ArrayList<>();

        while (resultSet.next()) {

            itemList.add(new Item(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)));
        }

        return itemList;
    }
}
