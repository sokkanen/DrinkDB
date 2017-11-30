package omat.drinkkidb.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import omat.drinkkidb.domain.Ingredient;

public class IngredientDao implements Dao<Ingredient, Integer> {

    private Database database;
    private Connection connection;
    private String tableName;

    public IngredientDao(Database database, String table) throws SQLException {
        this.database = database;
        this.tableName = table;
        this.connection = database.connect();
    }

    @Override
    public List<Ingredient> findAll() throws SQLException {
        List<Ingredient> ingredientList = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM " + this.tableName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ingredientList.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
        return ingredientList;
    }

    @Override
    public Ingredient findOne(Integer key) throws SQLException {
        String name = "";
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT name FROM " + this.tableName + " WHERE id = ?");
            stmt.setInt(1, key);
            ResultSet rs = stmt.executeQuery();
            name = rs.getString("name");
            rs.close();
            stmt.close();
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }

        return new Ingredient(key, name);
    }

    @Override
    public void remove(Integer key) throws SQLException {
        try {
            PreparedStatement stmt = this.connection.prepareStatement("DELETE FROM " + tableName + " WHERE id = ?");
            stmt.setInt(1, key);
            stmt.execute();
            stmt.close();
            database.disconnect();

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

    @Override
    public void saveOrUpdate(Ingredient ing) throws SQLException {
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT id, name FROM " + tableName + " WHERE " + tableName + ".name = ?");
            stmt.setString(1, ing.getName());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                stmt = this.connection.prepareStatement("INSERT INTO " + tableName + " (name) VALUES (?)");
                stmt.setString(1, ing.getName());
                stmt.execute();
            }
            rs.close();
            stmt.close();

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            System.out.println("Jossain mättää");
        }
    }

}
