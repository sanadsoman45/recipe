package com.example.recipe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import Adapter.DatabaseHandler;
import Adapter.MyAdapter;

public class Ingredients extends AppCompatActivity implements fragmenttoactivity{
    TextView counttv,butcounttv,myingtextview,ing_search;
    FrameLayout fl;
    AppBarLayout appbarlay;
    RelativeLayout rlv;
    Toolbar toolbar;
    private DatabaseHandler dbh;
    TextView tv1,tv2,seerecipe;
    LinearLayout linearlay;
    SharedPreferences shpmsgvar,shpmsgret;
    private  static  final  String sharedprefmsg="myprefsfile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        counttv=findViewById(R.id.counttv);
        butcounttv=findViewById(R.id.mypantrycount);
        dbh=new DatabaseHandler(getApplicationContext());
        linearlay=findViewById(R.id.bottommenulinear);
        appbarlay=findViewById(R.id.appbar);
        toolbar=findViewById(R.id.toolbar);
        fl=findViewById(R.id.flFragment);
        fl.setForeground(null);
        loadfragment(new Pantry());
        ing_search=findViewById(R.id.ing_col_search);

        shpmsgvar=getSharedPreferences(sharedprefmsg,0);
        SharedPreferences.Editor editor=shpmsgvar.edit();
        editor.putString("message","pantry");
        editor.commit();
        rlv=findViewById(R.id.relativecollapse);
        tv1=findViewById(R.id.mypantrytext);
        tv2=findViewById(R.id.mypantrycount);
        seerecipe=findViewById(R.id.seerecipe);

        appbarlay.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    rlv.setVisibility(View.VISIBLE);
                    rlv.setBackgroundColor(Color.parseColor("#D7456A"));
                    toolbar.setBackgroundColor(Color.parseColor("#D7456A"));

                } else {
                    rlv.setVisibility(View.GONE);
                    rlv.setBackgroundColor(0);
                    toolbar.setBackgroundColor(0);
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId())
                {

                    case R.id.navigation_Menu:
                        fragment=new Menu();
                        loadfragment(fragment);
                        linearlay.setVisibility(View.GONE);
                        break;

                    case R.id.navigation_favourites:
                        fragment=new Favourites();
                        loadfragment(fragment);
                        linearlay.setVisibility(View.GONE);
                        break;

                    case R.id.navigation_shoppinglist:
                        fragment=new Shoppinglist();
                        loadfragment(fragment);
                        linearlay.setVisibility(View.GONE);
                        break;

                    case R.id.navigation_pantry:
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            checkfrag(getWindow().getDecorView().getRootView());
                        }
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            checkPantry(getWindow().getDecorView().getRootView());
                        }
                        fragment=new Pantry();
                        loadfragment(fragment);
                        linearlay.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void popup(View v){
        @SuppressLint("RestrictedApi") MenuBuilder menuBuilder =new MenuBuilder(getApplicationContext());
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_items, menuBuilder);
        @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(this, menuBuilder, v);
        optionsMenu.setForceShowIcon(true);
        optionsMenu.show();
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                if(item.getItemId()==R.id.action_settings){
                    dbh.deleteallingredients();
                    butcounttv.setText(String.valueOf(dbh.get_count_ingredients()));
                    counttv.setText("You Have "+dbh.get_count_ingredients()+" Ingredients");
                    loadfragment(new Pantry());
                    Toast.makeText(Ingredients.this, "All ingredients deleted", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public void onMenuModeChange(@NonNull MenuBuilder menu) {

            }
        });
    }

    private void loadfragment(Fragment frag)
    {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment,frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void checkfrag(View v){
        shpmsgret=getSharedPreferences(sharedprefmsg,0);
        Log.d("msgtag","message is:"+shpmsgret.contains("message"));
        if(shpmsgret.contains("message"))
        {
            String message=shpmsgret.getString("message","notfound");
            Log.d("msgtag",message);
            Log.d("msg", String.valueOf(message=="pantry"));
            if(message.equals("pantry")){
                Log.d("msgtag","message is:"+message);
                SharedPreferences.Editor editor=shpmsgvar.edit();
                editor.putString("message","selectedingredients");
                loadfragment(new SelectedItem());
                toolbar.setVisibility(View.GONE);
                ing_search.setVisibility(View.GONE);
                tv1.setText("+ADD MORE RECIPES");
                tv2.setVisibility(View.GONE);
                editor.commit();
            }
            else if(message.equals("selectedingredients")){
                Log.d("msgtag","message is:"+message);
                SharedPreferences.Editor editor=shpmsgvar.edit();
                editor.putString("message","pantry");
                loadfragment(new Pantry());
                toolbar.setVisibility(View.VISIBLE);
                ing_search.setVisibility(View.VISIBLE);
                tv1.setText("My Pantry");
                tv2.setVisibility(View.VISIBLE);
                editor.commit();
            }
        }
    }
    public void checkPantry(View v){
        shpmsgret=getSharedPreferences(sharedprefmsg,0);
        if(shpmsgret.contains("message"))
        {
            String message=shpmsgret.getString("message","notfound");
            Log.d("msgtag",message);
            Log.d("msg", String.valueOf(message=="pantry"));
            if(message.equals("selectedingredients")){
                Log.d("msgtag",message);
                SharedPreferences.Editor editor=shpmsgvar.edit();
                editor.putString("message","pantry");
                loadfragment(new Pantry());
                toolbar.setVisibility(View.VISIBLE);
                ing_search.setVisibility(View.VISIBLE);
                tv1.setText("My Pantry");
                seerecipe.setBackgroundColor(Color.parseColor("#93C75B"));
                tv2.setVisibility(View.VISIBLE);
                editor.commit();
            }
        }
    }

    @Override
    public void communicate(int count) {
        butcounttv.setText(String.valueOf(count));
        counttv.setText("You Have "+count+" Ingredients");
    }
}