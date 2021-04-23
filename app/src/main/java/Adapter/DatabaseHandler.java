package Adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.recipe.R;

import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.Ingredients;
import Model.addtocart;
import Model.addtocartlist;
import Model.favourites;
import Model.menumodel;
import Model.searchfavouritesmodel;
import Model.selectedpantrymodel;
import Utils.Util;

public class DatabaseHandler extends SQLiteOpenHelper {
    private  Context context;
    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPE_TABLE="CREATE TABLE "+Util.TABLE_NAME+"("+Util.key_user_id+" varchar(50), "+Util.KEY_ingredientname+ " varchar(50),"+Util.KEY_ing_section_name+" VARCHAR(50))";
        String CREATE_CART_TABLE="CREATE TABLE "+Util.TABLE_NAME1+"("+Util.KEY_userid_addtocarttable+" varchar(50), "+Util.KEY_ingredientname_addtocarttable+ " varchar(50),"+Util.KEY_ing_section_name_addtocarttable+" VARCHAR(50))";
        String CREATE_FAV_TABLE="CREATE TABLE "+Util.TABLE_NAME2+"("+Util.KEY_userid_favtable+" VARCHAR(25),"+Util.KEY_recipe_name+" TEXT, "+Util.KEY_image_url+" TEXT, "+Util.KEY_ingredients+" TEXT, "+Util.KEY_instructions+" TEXT, "+Util.KEY_cook_time+" varchar(50), "+Util.KEY_prep_time+" varchar(50), "+Util.KEY_totaltime+" VARCHAR(50))";
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_RECIPE_TABLE);
        db.execSQL(CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Util.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ Util.TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS "+Util.TABLE_NAME2);
        onCreate(db);
    }

    public void addtofav(menumodel menumod){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Util.KEY_userid_favtable,menumod.getUser_id());
        values.put(Util.KEY_recipe_name,menumod.getRecipe_name());
        values.put(Util.KEY_ingredients,menumod.getIngredients());
        values.put(Util.KEY_instructions,menumod.getInstruction());
        values.put(Util.KEY_cook_time,menumod.getCook_time());
        values.put(Util.KEY_prep_time,menumod.getPrep_time());
        values.put(Util.KEY_totaltime,menumod.getTotal_time());
        values.put(Util.KEY_image_url,menumod.getImage_menufrag());
        db.insert(Util.TABLE_NAME2,null,values);
        db.close();
    }


    public ArrayList<favourites> get_allrecords_fav(String recipe_name,String user_id){
        ArrayList<favourites> menuitems=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT "+Util.KEY_userid_favtable+","+Util.KEY_recipe_name+","+Util.KEY_ingredients+","+Util.KEY_instructions+","+Util.KEY_cook_time+","+Util.KEY_prep_time+","+Util.KEY_image_url+","+Util.KEY_totaltime+" FROM "+Util.TABLE_NAME2+" WHERE "+Util.KEY_recipe_name+"=\""+recipe_name+"\""+" AND "+Util.KEY_userid_favtable+"=\""+user_id+"\"";
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        menuitems.add(new favourites(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7)));
        Log.d("arraylist","arraylist is:"+menuitems.size());
        return menuitems;
    }

    public ArrayList<favourites> get_records_fav(String user_id){
        ArrayList<favourites> menuitems=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select "+Util.KEY_recipe_name+","+Util.KEY_image_url+" FROM "+Util.TABLE_NAME2+" WHERE "+Util.KEY_userid_favtable+"=\""+user_id+"\"";
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        int i=0;
        while(!c.isAfterLast()){
            menuitems.add(new favourites(c.getString(0),c.getString(1)));
            c.moveToNext();
            i++;
        }
        Log.d("arraylist","arraylist is:"+menuitems.size());
        return menuitems;
    }

    public ArrayList<searchfavouritesmodel> get_recipes_fav(String user_id){
        ArrayList<searchfavouritesmodel> menuitems=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select "+Util.KEY_recipe_name+","+Util.KEY_image_url+","+Util.KEY_ingredients+" FROM "+Util.TABLE_NAME2+" WHERE "+Util.KEY_userid_favtable+"=\""+user_id+"\"";
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        int i=0;
        while(!c.isAfterLast()){
            menuitems.add(new searchfavouritesmodel(c.getString(0),c.getString(1),c.getString(2)));
            c.moveToNext();
            i++;
        }
        Log.d("arraylist","arraylist is:"+menuitems.size());
        return menuitems;
    }

    public int fav_table_count(String user_id){
        SQLiteDatabase sqldata=this.getWritableDatabase();
        String query="SELECT * FROM "+ Util.TABLE_NAME2+" WHERE "+Util.key_user_id+" = \""+user_id+"\"";
        Cursor cur=sqldata.rawQuery(query,null);
        return cur.getCount();
    }

    public void deleteingredient_fav(String user_id,String recipe_name){
        SQLiteDatabase sqldata=this.getWritableDatabase();
        String query="DELETE FROM "+ Util.TABLE_NAME2+" WHERE "+Util. KEY_recipe_name+" = \""+recipe_name+"\" and "+Util.key_user_id+" = \""+user_id+"\"";
//        sqldata.delete(Util.TABLE_NAME,Util.KEY_ingredientname+"=?",new String[]{String.valueOf(ingredient.getIngredient_name())});
        sqldata.execSQL(query);
    }

    public void empty_fav(String user_id){
        SQLiteDatabase sqldata=this.getWritableDatabase();
        String query="DELETE FROM "+ Util.TABLE_NAME2+" WHERE "+Util.key_user_id+" = \""+user_id+"\"";
//        sqldata.delete(Util.TABLE_NAME,Util.KEY_ingredientname+"=?",new String[]{String.valueOf(ingredient.getIngredient_name())});
        sqldata.execSQL(query);
    }

    public void addingredients(Ingredients ing){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Util.KEY_ingredientname,ing.getIngredient_name());
        values.put(Util.KEY_ing_section_name,ing.getIng_section_name());
        values.put(Util.key_user_id,ing.getUser_id());
        //Inserting values
        db.insert(Util.TABLE_NAME,null,values);
        db.close();
    }

    public boolean getIngredient_fav(String recipe_name,String user_id){
        boolean checkresult;
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+ Util.TABLE_NAME2+" WHERE "+Util.KEY_recipe_name+" = \""+recipe_name+"\" and "+Util.key_user_id+" = \""+user_id+"\"";
        Cursor cursor=db.rawQuery(query,null);
        if(cursor!=null){
            if(cursor.moveToFirst())
                checkresult= true;
            else
                checkresult=false;
        }
        else
            checkresult= false;
        return checkresult;
    }

    public int get_ing_section_count(String ing_section_name,String user_id){
        String query="SELECT * FROM "+ Util.TABLE_NAME+" WHERE "+Util.KEY_ing_section_name+" = \""+ing_section_name+"\" and "+Util.key_user_id+" = \""+user_id+"\"";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery(query,null);
        return cur.getCount();
    }



    //Get a ingredient
    /*public Ingredients getIngredient(String ingredient_name){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(Util.TABLE_NAME,new String[]{Util.KEY_ingredientname},Util.KEY_ingredientname+"=?",
                new String[]{ingredient_name},null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
            Ingredients ingredients=new Ingredients(cursor.getString(0));
            return ingredients;
    }*/

    public boolean getIngredient(String ingredient_name,String user_id){
        boolean checkresult;
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+ Util.TABLE_NAME+" WHERE "+Util.KEY_ingredientname+" = \""+ingredient_name+"\" and "+Util.key_user_id+" = \""+user_id+"\"";
        Cursor cursor=db.rawQuery(query,null);
        if(cursor!=null){
            if(cursor.moveToFirst())
                checkresult= true;
            else
                checkresult=false;
        }
        else
            checkresult= false;
        return checkresult;
    }

    public List<Ingredients> getAllingredients(){
        SQLiteDatabase db=this.getReadableDatabase();
        List<Ingredients> ing=new ArrayList<>();
        //Select all
        String selectall="SELECT * FROM "+Util.TABLE_NAME;
        Cursor cursor=db.rawQuery(selectall,null);
        //Loop through
        if(cursor.moveToFirst())
        {
            do{
                Ingredients ing_name=new Ingredients();
                ing_name.setIngredient_name(cursor.getString(0));
                //add ingredient name to our ingredients list
                ing.add(ing_name);
            }while (cursor.moveToNext());
        }
        return ing;
    }

    public void deleteingredient(Ingredients ingredient){
        SQLiteDatabase sqldata=this.getWritableDatabase();
        String query="DELETE FROM "+ Util.TABLE_NAME+" WHERE "+Util.KEY_ingredientname+" = \""+ingredient.getIngredient_name()+"\" and "+Util.key_user_id+" = \""+ingredient.getUser_id()+"\"";
//        sqldata.delete(Util.TABLE_NAME,Util.KEY_ingredientname+"=?",new String[]{String.valueOf(ingredient.getIngredient_name())});
        sqldata.execSQL(query);
        Log.d("deleteddata","deleted data is:"+ingredient.getIngredient_name());
    }

    public int get_count_ingredients(String userid){
        String query="SELECT * FROM "+ Util.TABLE_NAME+" WHERE "+Util.key_user_id+"= \""+userid+"\"";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery(query,null);
        return cur.getCount();
    }

    public void deleteallingredients(String userid){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="DELETE FROM "+Util.TABLE_NAME+" WHERE "+Util.key_user_id+"= \""+userid+"\"";
        Log.d("msgdelete","Delete ");
        db.execSQL(query);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public Drawable geticon(String section_name){
        Drawable icon=null;
        switch(section_name.toLowerCase()){
            case "meat":
                icon= ContextCompat.getDrawable(context, R.drawable.meat);
                break;

            case "sea food":
                icon=ContextCompat.getDrawable(context,R.drawable.seafood);
                break;

            case "dairy":
                icon=ContextCompat.getDrawable(context,R.drawable.milkcheese);
                break;

            case "vegetables":
                icon=ContextCompat.getDrawable(context,R.drawable.vegetables);
                break;

            case "fruits":
                icon=ContextCompat.getDrawable(context,R.drawable.fruits);
                break;

            case "baking and grains":
                icon=ContextCompat.getDrawable(context,R.drawable.bakingandgrains);
                break;

            case "condiments":
                icon=ContextCompat.getDrawable(context,R.drawable.condiments);
                break;

            case "oil":
                icon=ContextCompat.getDrawable(context,R.drawable.oils);
                break;

            case "nuts":
                icon=ContextCompat.getDrawable(context,R.drawable.nuts);
                break;
        }
        return icon;
    }

    public int get_ing_name(String ing_section,String user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query1 = "SELECT " + Util.KEY_ingredientname + " FROM " + Util.TABLE_NAME + " WHERE " + Util.KEY_ing_section_name
                + " = \"" + ing_section + "\" AND "+Util.key_user_id + "= \""+user_id+"\"";
        Cursor c1 = db.rawQuery(query1, null);
        return c1.getCount();
    }

    public String[] get_ingredients(String user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT "+Util.KEY_ingredientname+" FROM "+Util.TABLE_NAME+" WHERE "+ Util.key_user_id+"= \""+user_id+"\"";
        Cursor c = db.rawQuery(query , null);
        c.moveToFirst();
        String arr[]=new String[c.getCount()];
        int i=0;
        while (!c.isAfterLast()) {
            String ing_name = c.getString(0);
            arr[i]=ing_name;
            c.moveToNext();
            i++;
        }
        c.close();
        return arr;
    }

    public ArrayList<selectedpantrymodel> get_records(String user_id){
        ArrayList<selectedpantrymodel> selectedpantryitems=new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT DISTINCT "+Util.KEY_ing_section_name+" FROM "+Util.TABLE_NAME+" WHERE "+ Util.key_user_id+"= \""+user_id+"\"";
//        Log.d("msgtag",query);
        Cursor c = db.rawQuery(query , null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String section_name = c.getString(0);
//            Log.d("msgtag", section_name);
            String query1 = "SELECT " + Util.KEY_ingredientname + " FROM " + Util.TABLE_NAME + " WHERE " + Util.KEY_ing_section_name + " = \"" + section_name + "\" AND "+ Util.key_user_id+"= \""+user_id+"\"";
            Cursor c1 = db.rawQuery(query1, null);
            c1.moveToFirst();
            String ing_name[]=new String[c1.getCount()];
            int i=0;
            while (!c1.isAfterLast()) {
//                Log.d("msgtag", c1.getString(0));
                ing_name[i]=c1.getString(0);
                Log.d("delete1","Array is:"+ing_name[i]);
                c1.moveToNext();
                i++;
            }

            //if i add the below statement in above while loop then section name will be called many times so i created an arraylist and stored all the items of particular section
            //within arraylist and once the section's while loop completes one iteration it will clear the arraylist of ingredients.
            selectedpantryitems.add(new selectedpantrymodel(section_name, ing_name, geticon(section_name)));

            c.moveToNext();

        }
        return selectedpantryitems;
    }

    public void add_Ingredients_cart(addtocart add_cart){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Util.KEY_ingredientname_addtocarttable,add_cart.getIngredient_name());
        values.put(Util.KEY_ing_section_name_addtocarttable,add_cart.getIng_type());
        values.put(Util.KEY_userid_addtocarttable,add_cart.getUser_id());
        //Inserting values
        db.insert(Util.TABLE_NAME1,null,values);
        db.close();
    }

    public int get_count_ingredients_cart(String user_id){
        String query="SELECT * FROM "+ Util.TABLE_NAME1+" WHERE "+Util.KEY_userid_addtocarttable+"=\""+user_id+"\"";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery(query,null);
        Log.d("msgtag","ng count is:"+cur.getCount());
        return cur.getCount();
    }

    public void empty_cart(String user_id){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="DELETE FROM "+Util.TABLE_NAME1+" WHERE "+Util.KEY_userid_addtocarttable+"=\""+user_id+"\"";
        Log.d("msgdelete","Delete ");
        db.execSQL(query);
    }

    public void delete_purchased(String user_id){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="DELETE FROM "+Util.TABLE_NAME1 + " WHERE " + Util.KEY_ing_section_name_addtocarttable + " = \"purchased\""+" AND "+Util.KEY_userid_addtocarttable+"=\""+user_id+"\"";
        Log.d("msgdelete","Delete ");
        db.execSQL(query);
    }

    public ArrayList<addtocartlist> get_records_cart(String user_id){
        ArrayList<addtocartlist> addtocartlist=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query="Select "+Util.KEY_ingredientname_addtocarttable+" from "+Util.TABLE_NAME1+" WHERE "+Util.KEY_userid_addtocarttable+"=\""+user_id+"\"";
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        int i=0;
        String[] arr_names=new String[c.getCount()];
        while(!c.isAfterLast()){
            arr_names[i]=c.getString(0);
            c.moveToNext();
            i++;
        }
        addtocartlist.add(new addtocartlist(arr_names));
        return addtocartlist;
    }

    public String get_ingredient_type(String ingredient_name, String user_id){
        String type=" ";
        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT " + Util.KEY_ing_section_name_addtocarttable + " FROM " + Util.TABLE_NAME1
                + " WHERE " + Util.KEY_ingredientname_addtocarttable + " = \"" + ingredient_name + "\""
                +" AND "+Util.KEY_userid_addtocarttable+"=\""+user_id+"\"";

        Cursor cur=db.rawQuery(query,null);
        cur.moveToFirst();
        type=cur.getString(0);
        Log.d("msgtag","type is:"+cur.getString(0));
        Log.d("msgtag","type is:"+type);
        return type;
    }

    public boolean getIngredient_cart(String ingredient_name, String user_id){
        boolean checkresult;
        SQLiteDatabase db=this.getReadableDatabase();

        String query="SELECT " + Util.KEY_ingredientname_addtocarttable + " FROM " + Util.TABLE_NAME1
                + " WHERE " + Util.KEY_ingredientname_addtocarttable + " = \"" + ingredient_name + "\""
                +" AND "+Util.KEY_userid_addtocarttable+"=\""+user_id+"\"";

        Cursor cursor=db.rawQuery(query, null);
        if(cursor!=null){
            if(cursor.moveToFirst())
                checkresult= true;
            else
                checkresult=false;
        }
        else
            checkresult= false;
        Log.d("msgtag","Message is:"+checkresult);
        return checkresult;
    }

    public void deleteingredient_cart(addtocart add_cart){
        SQLiteDatabase sqldata=this.getWritableDatabase();
        Log.d("deletecartmsgtag","deleted item is:"+add_cart.getIngredient_name());
        Log.d("deletecartmsgtag","Query is:"+"DELETE FROM "+Util.TABLE_NAME1
                +" WHERE "+Util.KEY_userid_addtocarttable + " =\""+add_cart.getUser_id()+"\""
                +" AND " + Util.KEY_ingredientname_addtocarttable+ "= \""+add_cart.getIngredient_name()+"\"");
        sqldata.execSQL("DELETE FROM "+Util.TABLE_NAME1
                +" WHERE "+Util.KEY_userid_addtocarttable + " =\""+add_cart.getUser_id()+"\""
                +" AND " + Util.KEY_ingredientname_addtocarttable+ "= \""+add_cart.getIngredient_name()+"\"");
        sqldata.close();
    }

    public int get_ing_tobuy_count(){

        String query = "SELECT " + Util.KEY_ingredientname_addtocarttable + " FROM " + Util.TABLE_NAME1 + " WHERE " + Util.KEY_ing_section_name + " = tobuy";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery(query,null);
        return cur.getCount();
    }

    public int get_ing_purchased_count(){
        String query = "SELECT " + Util.KEY_ingredientname_addtocarttable + " FROM " + Util.TABLE_NAME1 + " WHERE " + Util.KEY_ing_section_name + " = purchased";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery(query,null);
        return cur.getCount();
    }

    public boolean update(String s, String s1,String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query="UPDATE "+Util.TABLE_NAME1+" SET "+Util.KEY_ing_section_name_addtocarttable+ " = \""+s+"\" WHERE " +Util.KEY_ingredientname_addtocarttable +" = \""+s1+"\""
                +"AND "+Util.KEY_userid_addtocarttable + " =\"" + user_id + "\"";
        db.execSQL(query);
        Log.d("msgtag",query);
        return true;
    }

}
