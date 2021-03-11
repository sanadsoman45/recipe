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

import java.util.ArrayList;
import java.util.List;

import Model.Ingredients;
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
        String CREATE_RECIPE_TABLE="CREATE TABLE "+Util.TABLE_NAME+"("+Util.KEY_ingredientname+ " varchar(50),"+Util.KEY_ing_section_name+" VARCHAR(50))";
        db.execSQL(CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Util.TABLE_NAME);
        onCreate(db);
    }

    public void addingredients(Ingredients ing){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Util.KEY_ingredientname,ing.getIngredient_name());
        values.put(Util.KEY_ing_section_name,ing.getIng_section_name());
        //Inserting values
        db.insert(Util.TABLE_NAME,null,values);
        db.close();
    }

    public int get_ing_section_count(String ing_section_name){
        String query="SELECT * FROM "+ Util.TABLE_NAME+" WHERE "+Util.KEY_ing_section_name+" = \""+ing_section_name+"\"";
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

    public boolean getIngredient(String ingredient_name){
        boolean checkresult;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(Util.TABLE_NAME,new String[]{Util.KEY_ingredientname},Util.KEY_ingredientname+"=?",
                new String[]{ingredient_name},null,null,null,null);
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
        sqldata.delete(Util.TABLE_NAME,Util.KEY_ingredientname+"=?",new String[]{String.valueOf(ingredient.getIngredient_name())});
        sqldata.close();
    }

    public int get_count_ingredients(){
        String query="SELECT * FROM "+ Util.TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery(query,null);
        return cur.getCount();
    }

    public void deleteallingredients(){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="DELETE FROM "+Util.TABLE_NAME;
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

    public int get_ing_name(String ing_section){
        SQLiteDatabase db = this.getReadableDatabase();
        String query1 = "SELECT " + Util.KEY_ingredientname + " FROM " + Util.TABLE_NAME + " WHERE " + Util.KEY_ing_section_name + " = \"" + ing_section + "\"";
        Cursor c1 = db.rawQuery(query1, null);
        return c1.getCount();
    }

    public ArrayList<selectedpantrymodel> get_records(){
        ArrayList<selectedpantrymodel> selectedpantryitems=new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT DISTINCT "+Util.KEY_ing_section_name+" FROM "+Util.TABLE_NAME;
//        Log.d("msgtag",query);
        Cursor c = db.rawQuery(query , null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String section_name = c.getString(0);
//            Log.d("msgtag", section_name);
            String query1 = "SELECT " + Util.KEY_ingredientname + " FROM " + Util.TABLE_NAME + " WHERE " + Util.KEY_ing_section_name + " = \"" + section_name + "\"";
            Cursor c1 = db.rawQuery(query1, null);
            c1.moveToFirst();
            String ing_name[]=new String[c1.getCount()];
            int i=0;
            while (!c1.isAfterLast()) {
//                Log.d("msgtag", c1.getString(0));
                ing_name[i]=c1.getString(0);
                c1.moveToNext();
                i++;
            }
            Log.d("msgtag","Array is:"+ing_name);
            //if i add the below statement in above while loop then section name will be called many times so i created an arraylist and stored all the items of particular section
            //within arraylist and once the section's while loop completes one iteration it will clear the arraylist of ingredients.
            selectedpantryitems.add(new selectedpantrymodel(section_name, ing_name, geticon(section_name)));
            c.moveToNext();
        }
        return selectedpantryitems;
    }
}
