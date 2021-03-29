package com.example.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter.DatabaseHandler;
import Adapter.searchFavourites;
import Adapter.searchadapter;
import Model.searchfavouritesmodel;
import Model.searchmodel;

public class Searchactivity_favourites extends AppCompatActivity {

    private RecyclerView.Adapter radp;
    SearchView searchbar;
    LinearLayout animatedlinearlay;
    FirebaseAuth fauth=FirebaseAuth.getInstance();
    FirebaseUser fuser=fauth.getCurrentUser();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),Ingredients.class);
        i.putExtra("fragname","favourites");
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchfavourites);
        Log.d("searchfrag","Hello");
        DatabaseHandler dbh=new DatabaseHandler(getApplicationContext());
        searchbar=findViewById(R.id.searchview);
        animatedlinearlay=findViewById(R.id.nocontlinearlay);
        RecyclerView recycleview=findViewById(R.id.recyclerview1);
        recycleview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchbar.setQueryHint("Search Something");
        if(searchbar.getQuery().toString().isEmpty()){
            recycleview.setVisibility(View.GONE);
            animatedlinearlay.setVisibility(View.VISIBLE);
        }
        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(searchbar.getQuery().toString().equals("")){
                    Log.d("msgtag","searchbar is empty");
                    searchbar.setQueryHint("Search Something");
                    recycleview.setVisibility(View.GONE);
                    animatedlinearlay.setVisibility(View.VISIBLE);
                }
                else if(!searchbar.getQuery().toString().isEmpty()){
                    recycleview.setVisibility(View.VISIBLE);
                    animatedlinearlay.setVisibility(View.GONE);
                    recycleview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    ArrayList<searchfavouritesmodel> arr=new ArrayList<>();
                    for(int i=0;i<dbh.get_recipes_fav(fuser.getUid()).size();i++){
                        if(dbh.get_recipes_fav(fuser.getUid()).get(i).getRecipe_name1().contains(searchbar.getQuery().toString().toLowerCase()) || dbh.get_recipes_fav(fuser.getUid()).get(i).getIngredients().contains(searchbar.getQuery().toString().toLowerCase())){
                            arr.add(new searchfavouritesmodel(dbh.get_recipes_fav(fuser.getUid()).get(i).getRecipe_name1(),dbh.get_recipes_fav(fuser.getUid()).get(i).getImage_url(),dbh.get_recipes_fav(fuser.getUid()).get(i).getIngredients()));
                        }
                    }

                    if(arr.size()<=0){
                        recycleview.setVisibility(View.GONE);
                        animatedlinearlay.setVisibility(View.VISIBLE);
                    }

                    radp=new searchFavourites(getBaseContext(),arr);
                    recycleview.setAdapter(radp);

                }
                return false;
            }
        });
    }
}