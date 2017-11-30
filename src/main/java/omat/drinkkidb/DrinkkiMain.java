package omat.drinkkidb;

import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.port;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import java.sql.*;
import java.util.Map;
import omat.drinkkidb.domain.Drink;
import omat.drinkkidb.domain.DrinkIngredient;
import omat.drinkkidb.domain.Ingredient;
import omat.drinkkidb.sql.Database;
import omat.drinkkidb.sql.DrinkDao;
import omat.drinkkidb.sql.DrinkIngredientDao;
import omat.drinkkidb.sql.IngredientDao;

public class DrinkkiMain {

    public static void main(String[] args) throws Exception {

        Database database = new Database("jdbc:sqlite:./db/drinks.db");
        DrinkDao drinks = new DrinkDao(database, "Drink");
        IngredientDao ingredients = new IngredientDao(database, "Ingredient");
        DrinkIngredientDao drinksingredients = new DrinkIngredientDao(database, "Drinkingredient", drinks, ingredients);

        // Port for HEROKU
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

        Spark.get("/drinks", (req, res) -> {
            Map map = new HashMap<>();
            map.put("drinks", drinks.findAll());
            map.put("ingredients", ingredients.findAll());
            return new ModelAndView(map, "drinks");
        }, new ThymeleafTemplateEngine());

        Spark.post("/drinks", (req, res) -> {
            Drink drink = new Drink(-1, req.queryParams("name"));
            drinks.saveOrUpdate(drink);
            res.redirect("/drinks");
            return "";
        });

        Spark.post("/removedrink", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("remove"));
            Map rmMap = new HashMap();
            rmMap.put(id, -1); // Will remove according to Drink_id, not ingredients.
            drinks.remove(id);
            drinksingredients.remove(rmMap);
            res.redirect("/drinks");
            return "";
        });

        Spark.post("/removeingredient", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("remove"));
            Map rmMap = new HashMap();
            rmMap.put(-1, id); // Will remove according to Ingredient_id, not drinks.
            ingredients.remove(id);
            drinksingredients.remove(rmMap);
            res.redirect("/ingredients");
            return "";
        });

        Spark.get("/drinks/:id", (req, res) -> {
            Map map = new HashMap<>();
            Integer drinkId = Integer.parseInt(req.params(":id"));
            map.put("drink", drinks.findOne(drinkId));
            map.put("drinkIngredients", drinksingredients.findAllbyDrink(drinkId));
            return new ModelAndView(map, "drink");
        }, new ThymeleafTemplateEngine());

        Spark.get("/ingredients", (req, res) -> {
            Map map = new HashMap<>();
            map.put("ingredients", ingredients.findAll());
            return new ModelAndView(map, "ingredients");
        }, new ThymeleafTemplateEngine());

        Spark.post("/ingredients", (req, res) -> {
            Ingredient ingredient = new Ingredient(-1, req.queryParams("name"));
            ingredients.saveOrUpdate(ingredient);
            res.redirect("/ingredients");
            return "";
        });

        Spark.get("/", (req, res) -> {
            Map map = new HashMap<>();
            map.put("drinks", drinks.findAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.post("/drinkingredients", (req, res) -> {
            int drinkId = Integer.parseInt(req.queryParams("drinkId"));
            int igId = Integer.parseInt(req.queryParams("ingredientId"));
            int order = Integer.parseInt(req.queryParams("orderNumber"));
            String amount = req.queryParams("amount");
            String instructions = req.queryParams("instructions");
            DrinkIngredient di = new DrinkIngredient("", drinkId, igId, order, amount, instructions);
            drinksingredients.saveOrUpdate(di);
            res.redirect("/drinks");
            return "";
        });

    }

}
