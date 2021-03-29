package Model;

public class searchfavouritesmodel {


    private String recipe_name1;

    private String image_url;

    private String ingredients;

    public searchfavouritesmodel(String recipe_name1, String image_url, String ingredients) {
        this.recipe_name1=recipe_name1;
        this.ingredients=ingredients;
        this.image_url=image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getRecipe_name1() {
        return recipe_name1;
    }

    public void setRecipe_name1(String recipe_name1) {
        this.recipe_name1 = recipe_name1;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}

