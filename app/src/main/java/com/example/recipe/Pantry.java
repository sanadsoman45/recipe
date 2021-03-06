package com.example.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Adapter.MyAdapter;
import Model.ListItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Pantry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pantry extends Fragment implements  Datatransferinterface{
    private RecyclerView.Adapter radp;
    private List<ListItem> listitem;
    fragmenttoactivity fragact;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragact= (fragmenttoactivity) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement fragment");
        }
    }

    public Pantry() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        fragact=null;
        super.onDetach();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Pantry.
     */
    // TODO: Rename and change types and number of parameters
    public static Pantry newInstance(String param1, String param2) {
        Pantry fragment = new Pantry();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("msg","hello from create view");
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_pantry, container, false);
        RecyclerView recycleview=rootview.findViewById(R.id.recyclerview1);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listitem=new ArrayList<>();
        
        HashMap<String, String[]> ingredients = new HashMap<String, String[]>();
        ingredients.put("Meat",new String[]{"Chicken breast","beacon", "sausage", "chicken drumsticks", "pork",
                "minced chicken", "chicken thighs", "chicken lollipop", "chicken gizzard", "chicken liver", "minced goat",
                "goat boneless", "mutton liver", "goat ribs and chops", "lamb ribs and chop", "goat shoulder", "mutton kidney"});
        ingredients.put("Sea Food",new String[]{"White compret", "seer", "indian salmon", "rohu", "basa", "catla","mackerel",
                "squid", "prawn", "sea bahh", "pink perch", "red fish snapper", "butter fish", "king fish", "yellow sin tuna", "hilsa",
                "bata", "lady fish", "black compret", "blue crab", "bombay duck", "tilapia", "marine cat fish", "kajoli", "murrell",
                "tiger prawn", "silver croaker", "blue spotted sting ray", "oyster", "lobster"
        });
        ingredients.put("Dairy",new String[]{ "Milk", "butter", "yogurt", "milk powder", "cream", "malai", "curd",
                "butter milk", "paneer", "khoya", "skimmed milk", "soya milk", "soya paneer", "mozzarella", "parmesan", "ricotta",
                "feta"
        });
        ingredients.put("Vegetables",new String[]{ "Onion", "tomato", "potato", "garlic", "ginger", "carrot", "capsicum", "mushroom",
                "green beans", "pea", "beet", "cauliflower", "cabbage", "coriander", "fenugreek", "spinach", "turnip", "sweet potato",
                "broccoli", "corn", "radish", "yam", "okra", "brinjal", "spring onion"
        });
        ingredients.put("Fruits",new String[]{"Coconut", "orange", "apple", "banana", "lime", "strawberry", "blueberry", "raspberry",
                "grapes", "peach", "mango", "pear", "blackberry", "date", "cherry", "kiwi", "dragon fruit", "pomegranate", "papaya",
                "guava", "passion fruit", "muskmelon", "watermelon", "plum", "fig"
        });
        ingredients.put("Baking And Grains",new String[]{"Rice", "pasta", "wheat", "flour", "maida", "bread", "baking powder",
                "baking soda", "corn starch", "bread crumb", "cocoa", "yeast", "brown rice", "pizza dough", "rice flour", "gram flour",
                "starch", "potato starch", "vermicelli", "noodle", "corn meal", "basmati rice", "barley"
        });
        ingredients.put("Condiments",new String[]{ "Mayonnaise", "tomato ketchup", "mustard sauce", "vinegar", "soya sauce", "barbeque sauce"
        });
        ingredients.put("Oil",new String[]{"Vegetable oil", "coconut oil", "peanut oil", "sunflower oil", "almond oil", "soya oil",
                "olive oil", "soyabean oil", "walnut oil"
        });
        ingredients.put("Nuts",new String[]{ "Peanut", "cashew", "almond", "walnut", "pistachios", "fried fig", "apricot",
                "hazelnut", "raisins", "black date"
        });

        if(listitem.size() > 0){
            listitem.clear();
        }

        for(String ing_section_name:ingredients.keySet())
        {
            ListItem item=new ListItem(ing_section_name, ingredients.get(ing_section_name));
            Log.d("msg", String.format("%s %s", ing_section_name, Arrays.toString(ingredients.get(ing_section_name))));
            listitem.add(item);
        }
        radp=new MyAdapter(getActivity(),listitem,this);
        recycleview.setAdapter(radp);
        return rootview;
    }

    @Override
    public void setcount(int count) {
        fragact.communicate(count);
        Log.d("msgtag","count from setcout is:"+count);
    }

}