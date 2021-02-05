package Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import Model.Ingredients;
import Utils.Util;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPE_TABLE="CREATE TABLE "+Util.TABLE_NAME+"("+Util.KEY_ingredientname+" varchar(50))";
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
        //Inserting values
        db.insert(Util.TABLE_NAME,null,values);
        db.close();
    }

    //Get a ingredient
    public Ingredients getIngredient(String ingredient_name){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(Util.TABLE_NAME,new String[]{Util.KEY_ingredientname},Util.KEY_ingredientname+"=?",
                new String[]{ingredient_name},null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
            Ingredients ingredients=new Ingredients(cursor.getString(0));
            return ingredients;
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
}
