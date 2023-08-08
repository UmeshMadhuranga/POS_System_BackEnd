package lk.ijse.gdse.pos_backend.dao.custom;

import lk.ijse.gdse.pos_backend.dao.CrudDAO;
import lk.ijse.gdse.pos_backend.dto.ItemDTO;
import lk.ijse.gdse.pos_backend.entity.Item;

import java.sql.SQLException;

public interface ItemDAO extends CrudDAO<Item, String> {
    public boolean updateQty(String  code, int qty) throws SQLException;
    public ItemDTO getItem(String code) throws SQLException;
}
