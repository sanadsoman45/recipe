package Model;

public class Ingredients {
    private String ingredient_name;
    private String ing_section_name;
    private String user_id;


    public Ingredients(String ingredient_name, String ing_section_name, String user_id) {
        this.ingredient_name = ingredient_name;
        this.ing_section_name = ing_section_name;
        this.user_id=user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getIng_section_name() {
        return ing_section_name;
    }

    public void setIng_section_name(String ing_section_name) {
        this.ing_section_name = ing_section_name;
    }


    public Ingredients() {
    }

    public Ingredients(String ingredient_name,String user_id) {
        this.ingredient_name = ingredient_name;
        this.user_id=user_id;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }
}
