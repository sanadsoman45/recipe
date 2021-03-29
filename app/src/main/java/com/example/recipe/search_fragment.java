package com.example.recipe;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.searchadapter;
import Adapter.selectedpantryitems;
import Model.searchmodel;

public class search_fragment extends AppCompatActivity {
    private RecyclerView.Adapter radp;
    SearchView searchbar;
    LinearLayout animatedlinearlay;
    ArrayList<searchmodel> arr_rec=new ArrayList<>();
    HashMap<String, String[]> item_map = new HashMap();
    private  static  final  String sharedprefmsg="myprefsfile";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences shpret=getSharedPreferences(sharedprefmsg,0);
        String message=shpret.getString("fragment_name","notfound");
        Log.d("msgfrag","message is:"+message);
        Intent i=new Intent(getApplicationContext(),Ingredients.class);
        if(shpret.contains("fragment_name"))
        {

            if(message.equals("pantry")){
                i.putExtra("fragname","pantry");
            }
            else if(message.equals("shoppinglist")){
                i.putExtra("fragname","shoppinglist");
            }

        }
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fragment);
        searchbar=findViewById(R.id.searchview);
        animatedlinearlay=findViewById(R.id.nocontlinearlay);
        RecyclerView recycleview=findViewById(R.id.recyclerview1);
        recycleview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchbar.setQueryHint("Search Something");
        if(searchbar.getQuery().toString().isEmpty()){
            recycleview.setVisibility(View.GONE);
            animatedlinearlay.setVisibility(View.VISIBLE);
        }


        item_map.put("Meat", new String[]{"Chicken breast", "beacon", "sausage", "chicken drumsticks", "pork", "minced chicken", "chicken thighs", "chicken lollipop", "chicken gizzard", "chicken liver", "minced goat", "goat boneless", "mutton livers", "goat ribs and chops", "lamb ribs and chop", "goat shoulder", "mutton kidney"});
        item_map.put("Sea Food", new String[]{"White compret", "seer", "indian salmon", "rohu", "basa", "catla", "mackerel", "squid", "prawn", "sea bahh", "pink perch", "red fish snapper", "butter fish", "king fish", "yellow sin tuna", "hilsa", "bata", "lady fish", "black compret", "blue crab", "bombay duck", "tilapia", "marine cat fish", "kajoli", "murrell", "tiger prawn", "silver croaker", "blue spotted sting ray", "oyster", "lobster"});
        item_map.put("Dairy", new String[]{"Milk", "butter", "yogurt", "milk powder", "cream", "malai", "curd", "butter milk", "paneer", "khoya", "skimmed milk", "soya milk", "soya paneer", "mozzarella", "parmesan", "ricotta", "feta"});
        item_map.put("Vegetables", new String[]{"Onion", "tomato", "potato", "garlic", "ginger", "carrot", "capsicum", "mushroom", "green beans", "pea", "beet", "cauliflower", "cabbage", "coriander", "fenugreek", "spinach", "turnip", "sweet potato", "broccoli", "corn", "radish", "yam", "okra", "brinjal", "spring onion"});
        item_map.put("Fruits", new String[]{"Coconut", "orange", "apple", "banana", "lime", "strawberry", "blueberry", "raspberry", "grapes", "peach", "mango", "pear", "blackberry", "date", "cherry", "kiwi", "dragon fruit", "pomegranate", "papaya", "guava", "passion fruit", "muskmelon", "watermelon", "plum", "fig"});
        item_map.put("Baking And Grains", new String[]{"Rice", "pasta", "wheat", "flour", "maida", "bread", "baking powder", "baking soda", "corn starch", "bread crumb", "cocoa", "yeast", "brown rice", "pizza dough", "rice flour", "gram flour", "starch", "potato starch", "vermicelli", "noodle", "corn meal", "basmati rice", "barley"});
        item_map.put("Condiments", new String[]{"Mayonnaise", "tomato ketchup", "mustard sauce", "vinegar", "soya sauce", "barbeque sauce"});
        item_map.put("Oil", new String[]{"Vegetable oil", "coconut oil", "peanut oil", "sunflower oil", "almond oil", "soya oil", "olive oil", "soyabean oil", "walnut oil"});
        item_map.put("Nuts", new String[]{"Peanut", "cashew", "almond", "walnut", "pistachios", "fried fig", "apricot", "hazelnut", "raisins", "black date"});

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("msgtag","inside onQuerytextchangedlistener");
                if(searchbar.getQuery().toString().equals("")){
                    Log.d("msgtag","searchbar is empty");
                    searchbar.setQueryHint("Search Something");
                    recycleview.setVisibility(View.GONE);
                    animatedlinearlay.setVisibility(View.VISIBLE);
                }
                else if(!searchbar.getQuery().toString().isEmpty()){
                    Log.d("msgtag","searchbar is not empty");
                    recycleview.setVisibility(View.VISIBLE);
                    animatedlinearlay.setVisibility(View.GONE);
                    recycleview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    if(arr_rec.size() > 0){
                        arr_rec.clear();
                    }



                    for (Map.Entry<String, String[]> mapEntry : item_map.entrySet()) {
                        for (int i=0;i<mapEntry.getValue().length;i++){
                            String ing_name=mapEntry.getValue()[i].toLowerCase();
                            if(ing_name.contains(searchbar.getQuery().toString().toLowerCase())){
                                arr_rec.add(new searchmodel(ing_name,mapEntry.getKey()));
                            }
                        }
                    }

                    Log.d("arraylist","arraylist is:"+arr_rec.toString());
                    radp=new searchadapter(getApplicationContext(),arr_rec);
                    recycleview.setAdapter(radp);

                }
                return false;
            }
        });

    }
}