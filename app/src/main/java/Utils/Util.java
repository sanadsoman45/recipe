package Utils;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Util {
    //Database Version
    public static final int DATABASE_VERSION=1;
    //Database name
    public static final String DATABASE_NAME="recipe";
    public static final String DATABASE_NAME_1="cart";
    //Table Name
    public static final String TABLE_NAME="ingredients_name";
    public static final String TABLE_NAME1="cart_table";
    public static final String TABLE_NAME2="favourites";
    //Table column names
    public static final String KEY_ingredientname="ingredient_name";
    public static final String KEY_ing_section_name="ing_section";
    public static final String key_user_id="user_id";

    public static final String KEY_ingredientname_addtocarttable="ingredient_name";
    public static final String KEY_ing_section_name_addtocarttable="ingredient_type";
    public static final String KEY_userid_addtocarttable="user_id";

    public static final String KEY_recipe_name="recipe_name";
    public static final String KEY_ingredients="ingredients";
    public static final String KEY_instructions="instructions";
    public static  final String KEY_cook_time="cook_time";
    public static final String KEY_prep_time="prep_time";
    public static final String KEY_image_url="image_url";
    public static final String KEY_totaltime="total_time";
    public static final String KEY_userid_favtable="user_id";


}
