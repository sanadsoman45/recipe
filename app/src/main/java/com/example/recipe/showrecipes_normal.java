package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Adapter.DatabaseHandler;
import Model.menumodel;

public class showrecipes_normal extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseUser fuser;
    ImageView food_image;
    TextView food_name;
    NestedScrollView nsv;
    FloatingActionButton fav_food;
    LinearLayout instructions_linear,ingredients_linear,totaltimelinearlay,cook_timelinerlay,prep_timelinearlay;
    TextView ingredients_content;
    TextView total_time_tv;
    TextView cook_time_tv;
    TextView prep_time_tv;
    TextView instructions_tv;
    int inst_count;
    int ing_count;
    String ingredients;
    String instructions;
    String cook_time;
    String prep_time;
    String total_time;
    String image_url;

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
        setContentView(R.layout.activity_showrecipes_normal);
        food_image=findViewById(R.id.food_img);

        fauth=FirebaseAuth.getInstance();
        fuser=fauth.getCurrentUser();
        instructions_linear=findViewById(R.id.instructions_section);
        instructions_tv=findViewById(R.id.instructions_content);
        fav_food=findViewById(R.id.fav_food);
        nsv=findViewById(R.id.this_food_nestscroll);
        DatabaseHandler dbh=new DatabaseHandler(getApplicationContext());
        food_name=findViewById(R.id.food_name);
        prep_timelinearlay=findViewById(R.id.prep_time_section);
        prep_time_tv=findViewById(R.id.prep_time_content);
        total_time_tv=findViewById(R.id.total_timecontent);
        totaltimelinearlay=findViewById(R.id.total_timesection);
        ingredients_content=findViewById(R.id.ingredients_content);
        ingredients_linear=findViewById(R.id.ingredient_section);
        cook_timelinerlay=findViewById(R.id.cook_time_section);
        cook_time_tv=findViewById(R.id.cook_time_content);


        nsv.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                fav_food.hide();
            } else {
                fav_food.show();
            }

        });

        String rec_name=getIntent().getStringExtra("recipe_name");
        if(rec_name!=null){
            inst_count=1;
            ing_count=1;
            ingredients="";
            instructions="";
            cook_time="";
            prep_time="";
            total_time="";
            image_url="";
            DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference().child("recipes");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot childSnapshot:snapshot.getChildren()) {

                        if(childSnapshot.getKey().equals(rec_name)){

                            if(dbh.getIngredient_fav(rec_name,fuser.getUid())){
                                fav_food.setImageTintList(ColorStateList.valueOf(Color.RED));
                            }

                            if(childSnapshot.child("ingredients").getValue() !=null){
                                ingredients_linear.setVisibility(View.VISIBLE);
                                ingredients=childSnapshot.child("ingredients").getValue().toString().toLowerCase();
                                String[] arrofing=ingredients.split("[0-9]+\\)");
                                for (String a : arrofing){
                                    if(!a.isEmpty()){
                                        ingredients_content.append(inst_count+")"+a+"\n\n");
                                        inst_count++;
                                    }
                                }
                            }

                            food_name.setText(rec_name);
                            instructions_linear.setVisibility(View.VISIBLE);
                            if(childSnapshot.child("instruction").getValue()!=null){
                                instructions_linear.setVisibility(View.VISIBLE);
                                instructions=childSnapshot.child("instruction").getValue().toString();
                                String[] arrofinst=instructions.split("[0-9]+\\)");
                                for (String a : arrofinst){
                                    if(!a.isEmpty()){
                                        Log.d("ingcounttag","count is:"+ing_count);
                                        instructions_tv.append(ing_count+")"+a+"\n\n");
                                        ing_count++;
                                    }
                                }
                            }

                            if(childSnapshot.child("prepare time").getValue()!=null){
                                prep_timelinearlay.setVisibility(View.VISIBLE);
                                prep_time=childSnapshot.child("prepare time").getValue().toString();
                                prep_time_tv.setText(prep_time);
                            }
                            if(childSnapshot.child("cook time").getValue()!=null){
                                cook_timelinerlay.setVisibility(View.VISIBLE);
                                cook_time=childSnapshot.child("cook time").getValue().toString();
                                cook_time_tv.setText(cook_time);
                            }
                            if(childSnapshot.child("total time").getValue()!=null){
                                totaltimelinearlay.setVisibility(View.VISIBLE);
                                total_time=childSnapshot.child("total time").getValue().toString();
                                total_time_tv.setText(total_time);
                            }
                            if(childSnapshot.child("image url").getValue()!=null){
                                image_url=childSnapshot.child("image url").getValue().toString();
                                Glide.with(getApplicationContext()).load(image_url).into(food_image);
                            }


                            fav_food.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!dbh.getIngredient_fav(rec_name,fuser.getUid())){
//                                        Log.d("ingtag1","ingredients is:"+ingredients);
//                                        Log.d("ingtag1","instructions is:"+instructions);
//                                        Log.d("ingtag1","cook_time is:"+cook_time);
//                                        Log.d("ingtag1","Prepare time is:"+prep_time);
//                                        Log.d("ingtag1","total_time is:"+total_time);
//                                        Log.d("ingtag1","image is:"+image_url);
                                        dbh.addtofav(new menumodel(fuser.getUid(),rec_name,ingredients,instructions,cook_time,prep_time,image_url,total_time));
                                        fav_food.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.heartpink)));
                                    }
                                    else{
                                        dbh.deleteingredient_fav(fuser.getUid(),rec_name);
                                        Toast.makeText(showrecipes_normal.this, "Deleted from fav", Toast.LENGTH_SHORT).show();
                                        fav_food.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D8D8D8")));
                                    }
                                }
                            });

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}