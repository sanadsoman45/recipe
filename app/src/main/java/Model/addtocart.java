package Model;

public class addtocart {
    private String ingredient_name;
    private String ing_type;
    private String user_id;

    public addtocart(String ingredient_name, String ing_type,String user_id) {
        this.ingredient_name = ingredient_name;
        this.ing_type = ing_type;
        this.user_id=user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public addtocart(String ingredient_name,String user_id) {
        this.ingredient_name = ingredient_name;
        this.user_id=user_id;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public String getIng_type() {
        return ing_type;
    }

}
