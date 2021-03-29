package com.example.recipe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Adapter.DatabaseHandler;
import Adapter.Favouritesadapter;
import Model.favourites;
import Model.menumodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favourites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favourites extends Fragment implements Favouriteinterface {

    DatabaseHandler dbh;
    LinearLayout linearlay;
    FirebaseAuth fauth=FirebaseAuth.getInstance();
    FirebaseUser fuser=fauth.getCurrentUser();
    private RecyclerView.Adapter radp;
    RecyclerView recycleview;
    private LinearLayout nonet,norecipes,noingredients;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Favourites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favourites.
     */
    // TODO: Rename and change types and number of parameters
    public static Favourites newInstance(String param1, String param2) {
        Favourites fragment = new Favourites();
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
        View rootview=inflater.inflate(R.layout.fragment_favourites, container, false);
        linearlay=rootview.findViewById(R.id.textlinear);
        dbh=new DatabaseHandler(getContext());
        ArrayList<favourites> arrlist=new ArrayList<>();
        recycleview=rootview.findViewById(R.id.recyclerview1);
        nonet=rootview.findViewById(R.id.nointernetconn);
        norecipes=rootview.findViewById(R.id.recipenotfound);
        noingredients=rootview.findViewById(R.id.nocontlinearlay);

        if(fuser.isAnonymous()){
            recycleview.setVisibility(View.GONE);
            nonet.setVisibility(View.GONE);
            noingredients.setVisibility(View.GONE);
            norecipes.setVisibility(View.VISIBLE);
        }

        else{
            if(dbh.fav_table_count(fuser.getUid())<=0){
                recycleview.setVisibility(View.GONE);
                nonet.setVisibility(View.GONE);
                noingredients.setVisibility(View.VISIBLE);
                norecipes.setVisibility(View.GONE);
                linearlay.setVisibility(View.GONE);
            }
            else{
                recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
                recycleview.setVisibility(View.VISIBLE);
                linearlay.setVisibility(View.VISIBLE);
                nonet.setVisibility(View.GONE);
                noingredients.setVisibility(View.GONE);
                norecipes.setVisibility(View.GONE);
                radp=new Favouritesadapter(getActivity(),dbh.get_records_fav(fuser.getUid()),this);
                recycleview.setAdapter(radp);
            }

        }
        return rootview;
    }

    @Override
    public void setcount(int count) {
        if(count<=0){
            recycleview.setVisibility(View.GONE);
            nonet.setVisibility(View.GONE);
            noingredients.setVisibility(View.VISIBLE);
            norecipes.setVisibility(View.GONE);
            linearlay.setVisibility(View.GONE);
        }
    }
}