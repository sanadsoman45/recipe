package Model;

public class favourites {
    String recipe_name;
    String instruction;
    String cook_time;
    String prep_time;
    String image;
    String total_time;
    String ingredients;
    String user_id;

    public favourites(String user_id,String recipe_name,String ingredients, String instruction, String cook_time, String prep_time, String image, String total_time) {
        this.recipe_name = recipe_name;
        this.instruction = instruction;
        this.cook_time = cook_time;
        this.prep_time = prep_time;
        this.image = image;
        this.user_id=user_id;
        this.total_time = total_time;
        this.ingredients=ingredients;
    }

    public favourites(String recipe_name,String image) {
        this.recipe_name = recipe_name;
        this.image=image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getCook_time() {
        return cook_time;
    }

    public void setCook_time(String cook_time) {
        this.cook_time = cook_time;
    }

    public String getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(String prep_time) {
        this.prep_time = prep_time;
    }

    public String getImage_menufrag() {
        return image;
    }

    public void setImage_menufrag(String image) {
        this.image = image;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }
}
