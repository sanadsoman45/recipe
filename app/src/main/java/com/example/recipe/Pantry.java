package com.example.recipe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adapter.MyAdapter;
import Model.ListItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Pantry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pantry extends Fragment {
    private RecyclerView.Adapter radp;
    private List<ListItem> listitem;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Pantry() {
        // Required empty public constructor
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
        String[] arr={"Dairy","Meat","Poultry"};
        String[] dairy_btn={"Milk","Butter","Butter","Butter","Yoghurt"};

        Log.d("msg","Array length is:"+arr.length);
        for(int i=0;i<arr.length;i++){
            ListItem item=new ListItem(""+arr[i],dairy_btn);
            listitem.add(item);
            radp=new MyAdapter(getActivity(),listitem);
            recycleview.setAdapter(radp);
        }
        return rootview;
    }
}