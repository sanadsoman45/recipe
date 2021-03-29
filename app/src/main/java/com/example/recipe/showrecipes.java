package com.example.recipe;

import androidx.appcompat.app.AppCompatActivity;
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

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Adapter.DatabaseHandler;
import Model.favourites;

public class showrecipes extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseUser fuser;
    ImageView food_image;
    TextView food_name;
    NestedScrollView nsv;
    FloatingActionButton fav_food;
    LinearLayout instructions_linear,ingredients_linear,totaltimelinearlay,cook_timelinerlay,prep_timelinearlay;
    TextView ingredients_content;
    TextView total_time;
    TextView cook_time;
    TextView prep_time;
    TextView instructions;

    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),Ingredients.class);
        i.putExtra("fragname","favourites");
        startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showrecipes);
        food_image=findViewById(R.id.food_img);
        fauth=FirebaseAuth.getInstance();
        fuser=fauth.getCurrentUser();
        instructions_linear=findViewById(R.id.instructions_section);
        instructions=findViewById(R.id.instructions_content);
        fav_food=findViewById(R.id.fav_food);
        nsv=findViewById(R.id.this_food_nestscroll);
        DatabaseHandler dbh=new DatabaseHandler(getApplicationContext());
        food_name=findViewById(R.id.food_name);
        prep_timelinearlay=findViewById(R.id.prep_time_section);
        prep_time=findViewById(R.id.prep_time_content);
        total_time=findViewById(R.id.total_timecontent);
        totaltimelinearlay=findViewById(R.id.total_timesection);
        ingredients_content=findViewById(R.id.ingredients_content);
        ingredients_linear=findViewById(R.id.ingredient_section);
        cook_timelinerlay=findViewById(R.id.cook_time_section);
        cook_time=findViewById(R.id.cook_time_content);
        nsv.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                fav_food.hide();
            } else {
                fav_food.show();
            }

        });



        String rec_name=getIntent().getStringExtra("recipe_name");
        if(rec_name!=null){

            ArrayList<favourites> fav=dbh.get_allrecords_fav(rec_name,fuser.getUid());
            int ing_count=1;
            int inst_count=1;
            for(int i=0;i<fav.size();i++){
                Glide.with(getApplicationContext()).load(fav.get(i).getImage_menufrag()).into(food_image);
                food_name.setText(fav.get(i).getRecipe_name());

                if(dbh.getIngredient_fav(rec_name,fuser.getUid())){
                    fav_food.setImageTintList(ColorStateList.valueOf(Color.RED));
                }
                fav_food.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbh.deleteingredient_fav(fuser.getUid(),rec_name);
                        Intent i=new Intent(getApplicationContext(),Ingredients.class);
                        i.putExtra("fragname","favourites");
                        startActivity(i);
                    }
                });

                if(fav.get(i).getPrep_time() !=null && !fav.get(i).getPrep_time().isEmpty()){
                    prep_timelinearlay.setVisibility(View.VISIBLE);
                    prep_time.setText(fav.get(i).getPrep_time());
                }

                if(fav.get(i).getCook_time()!=null && !fav.get(i).getCook_time().isEmpty()){
                    cook_timelinerlay.setVisibility(View.VISIBLE);
                    cook_time.setText(fav.get(i).getCook_time());
                }

                if(fav.get(i).getTotal_time()!=null && !fav.get(i).getTotal_time().isEmpty()){
                    totaltimelinearlay.setVisibility(View.VISIBLE);
                    total_time.setText(fav.get(i).getTotal_time());
                }

                if(fav.get(i).getIngredients()!=null && !fav.get(i).getIngredients().isEmpty() ){
                    ingredients_linear.setVisibility(View.VISIBLE);
                    String[] arrofing=fav.get(i).getIngredients().split("[0-9]+\\)");
                    for (String a : arrofing){
                        if(!a.isEmpty()){
                            Log.d("ingcounttag","count is:"+ing_count);
                            ingredients_content.append(inst_count+")"+a+"\n\n");
                            inst_count++;
                        }
                    }
                }

                if(fav.get(i).getInstruction()!=null && !fav.get(i).getInstruction().isEmpty() ){
                    instructions_linear.setVisibility(View.VISIBLE);
                    String[] arrofinst=fav.get(i).getInstruction().split("[0-9]+\\)");
                    for (String a : arrofinst){
                        if(!a.isEmpty()){
                            Log.d("ingcounttag","count is:"+ing_count);
                            instructions.append(ing_count+")"+a+"\n\n");
                            ing_count++;
                        }
                    }
                }

                Log.d("favmsgtag","ing name is:"+fav.get(i).getImage_menufrag());
                Log.d("favmsgtag","ing name is:"+fav.get(i).getInstruction());
                Log.d("favmsgtag","ing name is:"+fav.get(i).getTotal_time());
                Log.d("favmsgtag","ing name is:"+fav.get(i).getPrep_time());
                Log.d("favmsgtag","ing name is:"+fav.get(i).getCook_time());
                Log.d("favmsgtag","ing name is:"+fav.get(i).getRecipe_name());
            }
        }

    }
}