package lk.ijse.gdse.pos_backend.dao;

import lk.ijse.gdse.pos_backend.entity.SuperEntity;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface CrudDAO <T extends SuperEntity, ID extends Serializable> extends SuperDAO{
    List<T> getAll() throws SQLException;

    T save( T entity) throws SQLException;

    T update(T entity) throws SQLException;

    void delete(ID pk) throws SQLException;

}
