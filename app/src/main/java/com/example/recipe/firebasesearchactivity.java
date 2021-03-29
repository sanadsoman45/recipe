package com.example.recipe;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.DatabaseHandler;
import Adapter.Firebasesearchadapter;
import Model.firebasesearchmodel;
import Model.menumodel;

public class firebasesearchactivity extends AppCompatActivity {

    private RecyclerView.Adapter radp;
    SearchView searchbar;
    LinearLayout animatedlinearlay;
    FirebaseAuth fauth=FirebaseAuth.getInstance();
    FirebaseUser fuser=fauth.getCurrentUser();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),Ingredients.class);
        i.putExtra("fragname","menu");
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebasesearchactivity);
        searchbar=findViewById(R.id.searchview);
        animatedlinearlay=findViewById(R.id.nocontlinearlay);
        RecyclerView recycleview=findViewById(R.id.recyclerview1);
        recycleview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchbar.setQueryHint("Search Something");
        if(searchbar.getQuery().toString().isEmpty()){
            Log.d("msgarr1","msgtag is:"+searchbar.getQuery().toString());
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
                    Log.d("querymsg",newText);
                    ArrayList<firebasesearchmodel> arrlist=new ArrayList<>();
                    recycleview.setVisibility(View.VISIBLE);
                    animatedlinearlay.setVisibility(View.GONE);
                    recycleview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference().child("recipes");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot childSnapshot:snapshot.getChildren()) {
                                String ingredients="";
                                if(childSnapshot.child("ingredients").getValue() !=null){
                                    ingredients=childSnapshot.child("ingredients").getValue().toString();
                                }
                                String recipe_name= childSnapshot.getKey();
                                String cook_time="";
                                String prep_time="";
                                String total_time="";
                                Log.d("querymsg","Query msg is:"+searchbar.getQuery().toString().toLowerCase());
                                Log.d("containsmsg","Recipe name is:"+recipe_name+"Contains ingredients:"+ingredients.toLowerCase().contains(searchbar.getQuery().toString().toLowerCase()));
                                Log.d("containsmsg","Recipe name is:"+recipe_name+"Contains ingredients:"+recipe_name.toLowerCase().contains(searchbar.getQuery().toString().toLowerCase()));
                                if(!ingredients.isEmpty()){
                                    if(ingredients.toLowerCase().contains(searchbar.getQuery().toString().toLowerCase()) || recipe_name.toLowerCase().contains(searchbar.getQuery().toString().toLowerCase())){

                                        String instruction=childSnapshot.child("instruction").getValue().toString();
                                        Log.d("recmsg","Recipe name is:"+recipe_name.toLowerCase());
                                        if(childSnapshot.child("prepare time").getValue()!=null){
                                            prep_time = childSnapshot.child("prepare time").getValue().toString();
                                        }
                                        if(childSnapshot.child("cook time").getValue()!=null){
                                            cook_time=childSnapshot.child("cook time").getValue().toString();
                                        }
                                        if(childSnapshot.child("totaltime").getValue()!=null){
                                            total_time=childSnapshot.child("totaltime").getValue().toString();
                                        }
                                        Log.d("array","Elements are:"+recipe_name);
                                        String image=childSnapshot.child("image url").getValue().toString();
                                        arrlist.add(new firebasesearchmodel(fuser.getUid(),recipe_name,ingredients,instruction,cook_time,prep_time,image,total_time));
                                    }
                                }
                            }

                            radp=new Firebasesearchadapter(getApplicationContext(),arrlist);
                            recycleview.setAdapter(radp);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                return false;
            }
        });

    }
}