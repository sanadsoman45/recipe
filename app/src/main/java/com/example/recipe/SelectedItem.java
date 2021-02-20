package com.example.recipe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

import Adapter.DatabaseHandler;
import Model.Ingredients;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectedItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectedItem extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView t,t1;
    DatabaseHandler dbh;
    LinearLayout layout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectedItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectedItem.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectedItem newInstance(String param1, String param2) {
        SelectedItem fragment = new SelectedItem();
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

    @SuppressLint({"SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_selected_item, container, false);
        layout=view.findViewById(R.id.l1layout);
        dbh=new DatabaseHandler(getContext());
        List<Ingredients> ing_list=dbh.getAllingredients();
        for(Ingredients ing:ing_list){
            String all_ing=ing.getIngredient_name();
            for(int i=0;i<=dbh.get_count_ingredients();i++) {
                t1=new TextView(getContext());
                t1.setPadding(50, 0, 0, 0);
                t1.setTextSize(20);
                t1.append("\n" + all_ing.toLowerCase());
                Log.d("all_ing", "All Ing: " + all_ing);
            }
            layout.addView(t1);
        }
        return view;
    }
}