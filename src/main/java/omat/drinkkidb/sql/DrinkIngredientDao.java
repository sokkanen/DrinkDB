package omat.drinkkidb.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import omat.drinkkidb.domain.DrinkIngredient;

public class DrinkIngredientDao implements Dao<DrinkIngredient, Map<Integer, Integer>> {

    private DrinkDao drinkdao;
    private IngredientDao ingredientdao;
    private Database database;
    private Connection connection;
    private String tableName;

    public DrinkIngredientDao(Database database, String tableName, DrinkDao drinkdao, IngredientDao ingredientdao) throws SQLException {
        this.database = database;
        this.drinkdao = drinkdao;
        this.ingredientdao = ingredientdao;
        this.connection = this.database.connect();
        this.tableName = tableName;
    }

    @Override
    public void saveOrUpdate(DrinkIngredient di) throws SQLException {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT  drink_id, ingredient_id FROM " + tableName
                    + " WHERE drink_id = ? AND ingredient_id = ?");
            stmt.setInt(1, di.getDrink());
            stmt.setInt(2, di.getIngredient());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                stmt = connection.prepareStatement("INSERT INTO " + tableName
                        + " (drink_id, ingredient_id, amount, ordernumber, instructions) VALUES (?,?,?,?,?)");
                stmt.setInt(1, di.getDrink());
                stmt.setInt(2, di.getIngredient());
                stmt.setString(3, di.getAmount());
                stmt.setInt(4, di.getOrderNumber());
                stmt.setString(5, di.getInstructions());
                stmt.execute();
            }
            rs.close();
            stmt.close();
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

    public List<DrinkIngredient> findAllbyDrink(int drinkId) throws SQLException {
        List<DrinkIngredient> diList = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM " + this.tableName + " WHERE drink_id = ?");
            stmt.setInt(1, drinkId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = ingredientdao.findOne(rs.getInt("ingredient_id")).getName();
                diList.add(new DrinkIngredient(name, rs.getInt("drink_id"), rs.getInt("ingredient_id"), rs.getInt("ordernumber"), rs.getString("amount"), rs.getString("instructions")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }

        for (int i = 0; i < diList.size(); i++) {
            System.out.println(diList.get(i).getOrderNumber());
        }

        List<DrinkIngredient> diSorted = diList.stream().sorted().collect(Collectors.toCollection(ArrayList::new));

        for (int i = 0; i < diSorted.size(); i++) {
            System.out.println(diSorted.get(i).getOrderNumber());
        }

        return diSorted;
    }

    @Override
    public List<DrinkIngredient> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DrinkIngredient findOne(Map<Integer, Integer> key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Map<Integer, Integer> key) throws SQLException {
        int drinkId = key.keySet().stream().findFirst().get();
        int ingredientId = key.values().stream().findFirst().get();

        // Set undesired to -1
        try {

            PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + tableName + " WHERE drink_id = ? OR ingredient_id = ?");
            stmt.setInt(1, drinkId);
            stmt.setInt(2, ingredientId);
            stmt.execute();
            stmt.close();

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
    }

}
