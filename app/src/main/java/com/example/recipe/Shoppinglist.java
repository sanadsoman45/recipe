package com.example.recipe;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import java.util.List;

import Adapter.DatabaseHandler;
import Adapter.cartitems;
import Adapter.selectedpantryitems;
import Model.ListItem;
import Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Shoppinglist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Shoppinglist extends Fragment implements Datainterface_shoppinglist {

    private RecyclerView.Adapter radp;
    private LinearLayout animatelinearlay;
    private DatabaseHandler dbh;
    int count=0;
    private RecyclerView recycleview;
    fragtoactivityshoppinglist fragact;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragact= (fragtoactivityshoppinglist) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement fragment");
        }
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Shoppinglist() {
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
     * @return A new instance of fragment Shoppinglist.
     */
    // TODO: Rename and change types and number of parameters
    public static Shoppinglist newInstance(String param1, String param2) {
        Shoppinglist fragment = new Shoppinglist();
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
        View rootview=inflater.inflate(R.layout.fragment_shoppinglist, container, false);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        animatelinearlay=rootview.findViewById(R.id.nocontlinearlay);
        recycleview=rootview.findViewById(R.id.recyclerview1);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        dbh=new DatabaseHandler(getContext());
        Log.d("mstag","Array is:"+dbh.get_records(firebaseUser.getUid()));
        Log.d("msgtag","length is:"+dbh.get_count_ingredients(firebaseUser.getUid()));
        if(dbh.get_count_ingredients_cart(firebaseUser.getUid())>0){
            recycleview.setVisibility(View.VISIBLE);
            animatelinearlay.setVisibility(View.GONE);
            Log.d("msgtag","records are:"+dbh.get_records_cart(firebaseUser.getUid()));
            radp=new cartitems(getActivity(),dbh.get_records_cart(firebaseUser.getUid()),this);
            recycleview.setAdapter(radp);
        }
        else{
            recycleview.setVisibility(View.GONE);
            animatelinearlay.setVisibility(View.VISIBLE);
        }
        return rootview;
    }

    public void setcount(int count) {
        fragact.communicate_shoppinglist(count);
        this.count=count;
        Log.d("count","count from shoppinglist  is:"+count);
        Log.d("msgtag","count from setcout is:"+count);
        if(count<=0){
            recycleview.setVisibility(View.GONE);
            animatelinearlay.setVisibility(View.VISIBLE);
        }
    }


}