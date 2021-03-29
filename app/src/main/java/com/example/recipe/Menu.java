package com.example.recipe;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import Adapter.DatabaseHandler;
import Adapter.selectedpantryitems;
import Model.ListItem;
import Model.menumodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menu extends Fragment {

    DatabaseHandler dbh;
    int count;
    LinearLayout linearlay;
    FirebaseAuth fauth=FirebaseAuth.getInstance();
    FirebaseUser fuser=fauth.getCurrentUser();
    private RecyclerView.Adapter radp;
    RecyclerView recycleview;
    private LinearLayout nonet,norecipes,noingredients,loading;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Menu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Menu.
     */
    // TODO: Rename and change types and number of parameters
    public static Menu newInstance(String param1, String param2) {
        Menu fragment = new Menu();
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
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_menu, container, false);
        ArrayList<menumodel> arrlist=new ArrayList<>();
        ArrayList<String> arr_recname=new ArrayList<>();
        linearlay=rootview.findViewById(R.id.textlinear);
        loading=rootview.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        count=0;
        dbh=new DatabaseHandler(getContext());
        if(arrlist.size() > 0){
            arrlist.clear();
        }
        recycleview=rootview.findViewById(R.id.recyclerview1);
        nonet=rootview.findViewById(R.id.nointernetconn);
        norecipes=rootview.findViewById(R.id.recipenotfound);
        noingredients=rootview.findViewById(R.id.nocontlinearlay);

        Log.d("arr","Array is:"+ Arrays.toString(dbh.get_ingredients(fuser.getUid())));



        if(dbh.get_count_ingredients(fuser.getUid())<=0){
            recycleview.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            nonet.setVisibility(View.GONE);
            noingredients.setVisibility(View.VISIBLE);
            norecipes.setVisibility(View.GONE);

        }

        else{
            DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference().child("recipes");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot childSnapshot:snapshot.getChildren()) {
                        String ingredients="";
                        if(childSnapshot.child("ingredients").getValue() !=null){
                            ingredients=childSnapshot.child("ingredients").getValue().toString().toLowerCase();
                        }

                        String arr[]=dbh.get_ingredients(fuser.getUid());
                        String cook_time="";
                        String prep_time="";
                        String total_time="";
                        for(int i=0;i<arr.length;i++){
                            if(!ingredients.isEmpty()){
                                if(ingredients.contains(arr[i])){

                                    String recipe_name= childSnapshot.getKey().toLowerCase();
                                    if(!arr_recname.contains(recipe_name)){
                                        String instruction=childSnapshot.child("instruction").getValue().toString();
                                        if(childSnapshot.child("prepare time").getValue()!=null){
                                            prep_time = childSnapshot.child("prepare time").getValue().toString();
                                        }
                                        if(childSnapshot.child("cook time").getValue()!=null){
                                            cook_time=childSnapshot.child("cook time").getValue().toString();
                                        }
                                        if(childSnapshot.child("total time").getValue()!=null){
                                            total_time=childSnapshot.child("total time").getValue().toString();
                                        }
                                        String image=childSnapshot.child("image url").getValue().toString();
                                        arrlist.add(new menumodel(fuser.getUid(),recipe_name,ingredients,instruction,cook_time,prep_time,image,total_time));
                                        arr_recname.add(recipe_name);
                                    }
                                }
                            }
                        }
                    }



//                    for(int i=0;i<arrlist.size();i++){
//                        Log.d("ingtagmsg","Elements are:"+arrlist.get(i).getRecipe_name());
//                    }

                    if(arrlist.size()<=0){
                        recycleview.setVisibility(View.GONE);
                        nonet.setVisibility(View.GONE);
                        noingredients.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        norecipes.setVisibility(View.VISIBLE);
                    }
                    else{
                        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
                        recycleview.setVisibility(View.VISIBLE);
                        linearlay.setVisibility(View.VISIBLE);
                        nonet.setVisibility(View.GONE);
                        noingredients.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        norecipes.setVisibility(View.GONE);
                        radp=new Adapter.Menu(getActivity(),arrlist);
                        recycleview.setAdapter(radp);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


//
//        Log.d("fuser","User is:"+fuser.getUid());
        Log.d("fuser","User is:"+arrlist.size());

        return rootview;
    }
}