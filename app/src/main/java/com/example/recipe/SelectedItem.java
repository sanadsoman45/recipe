package com.example.recipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adapter.DatabaseHandler;
import Adapter.MyAdapter;
import Adapter.selectedpantryitems;
import Model.Ingredients;
import Model.ListItem;

public class SelectedItem extends Fragment implements  Datatransferinterface {
    DatabaseHandler dbh;
    private RecyclerView.Adapter radp;
    fragmenttoactivity fragact;
    private LinearLayout animatelinearlay;

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

    public SelectedItem() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        fragact=null;
        super.onDetach();
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("msg","hello from create view");

        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_selected_item, container, false);
        animatelinearlay=rootview.findViewById(R.id.nocontlinearlay);
        RecyclerView recycleview=rootview.findViewById(R.id.recyclerview1);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        dbh=new DatabaseHandler(getContext());
        Log.d("mstag","Array is:"+dbh.get_records());
        Log.d("msgtag","length is:"+dbh.get_count_ingredients());
        if(dbh.get_count_ingredients()>0){
            recycleview.setVisibility(View.VISIBLE);
            animatelinearlay.setVisibility(View.GONE);
            radp=new selectedpantryitems(getActivity(),dbh.get_records(),this);
            recycleview.setAdapter(radp);
        }
        else{
            recycleview.setVisibility(View.GONE);
            animatelinearlay.setVisibility(View.VISIBLE);
        }
        return rootview;
    }

    @Override
    public void setcount(int count) {
        fragact.communicate(count);
        Log.d("msgtag","count from setcout is:"+count);
    }
}