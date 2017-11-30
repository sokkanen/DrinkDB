package omat.drinkkidb.domain;

public class DrinkIngredient implements Comparable<DrinkIngredient> {

    private String name;
    private int drink;
    private int ingredient;
    private String amount;
    private int orderNumber;
    private String instructions;

    public DrinkIngredient(String name, int drink, int ingredient, int orderNumber, String amount, String instructions) {
        this.drink = drink;
        this.ingredient = ingredient;
        this.orderNumber = orderNumber;
        this.amount = amount;
        this.instructions = instructions;
        this.name = name;
    }

    public int getDrink() {
        return drink;
    }

    public int getIngredient() {
        return ingredient;
    }

    public String getAmount() {
        return amount;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(DrinkIngredient t) {
        if (t.orderNumber < this.orderNumber) {
            return t.orderNumber;
        }
        return this.orderNumber;
    }

}
