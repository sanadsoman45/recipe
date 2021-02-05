package Model;

public class Ingredients {
    private String ingredient_name;
    private int id;

    public Ingredients() {
    }

    public Ingredients(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }
}
