package omat.drinkkidb.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import omat.drinkkidb.domain.Drink;

public class DrinkDao implements Dao<Drink, Integer> {

    private Database database;
    private String tableName;

    public DrinkDao(Database database, String tableName) throws SQLException {
        this.database = database;
        this.tableName = tableName;
    }

    @Override
    public List<Drink> findAll() throws SQLException {
        List<Drink> drinks = new ArrayList<>();
        try {
            Connection connection = database.connect();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM " + this.tableName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                drinks.add(new Drink(Integer.parseInt(rs.getString("id")), rs.getString("name")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
        this.database.disconnect();
        return drinks;
    }

    @Override
    public Drink findOne(Integer key) throws SQLException {
        String name = "";
        try {
            Connection connection = database.connect();
            PreparedStatement stmt = connection.prepareStatement("SELECT name FROM " + this.tableName + " WHERE id = ?");
            stmt.setInt(1, key);
            ResultSet rs = stmt.executeQuery();
            name = rs.getString("name");
            rs.close();
            stmt.close();
            database.disconnect();
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
        
        return new Drink(key, name);
    }

    @Override
    public void remove(Integer key) throws SQLException {
        try {
            Connection connection = database.connect();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + tableName + " WHERE id = ?");
            stmt.setInt(1, key);
            stmt.execute();
            stmt.close();
            database.disconnect();

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

    @Override
    public void saveOrUpdate(Drink drink) throws SQLException {
        try {
            Connection conn = database.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE " + tableName + ".name = ?");
            stmt.setString(1, drink.getName());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                stmt = conn.prepareStatement("INSERT INTO " + tableName + " (name) VALUES (?)");
                stmt.setString(1, drink.getName());
                stmt.execute();
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }
}
