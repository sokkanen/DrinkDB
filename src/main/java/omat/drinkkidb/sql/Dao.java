package omat.drinkkidb.sql;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T, K> {

    List<T> findAll() throws SQLException;

    T findOne(K key) throws SQLException;

    void remove(K key) throws SQLException;

    void saveOrUpdate(T unit) throws SQLException;

}
