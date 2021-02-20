package com.example.recipe;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class Ingredients extends AppCompatActivity {

    FrameLayout fl;
    AppBarLayout appbarlay;
    RelativeLayout rlv;
    Toolbar toolbar;
    private DatabaseHandler dbh;
    TextView tv1,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dbh=new DatabaseHandler(this);

        appbarlay=findViewById(R.id.appbar);
        toolbar=findViewById(R.id.toolbar);
        fl=findViewById(R.id.flFragment);
        fl.setForeground(null);
        loadfragment(new Pantry());
        rlv=findViewById(R.id.relativecollapse);
        tv1=findViewById(R.id.mypantrytext);
        tv2=findViewById(R.id.mypantrycount);

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
                        break;

                    case R.id.navigation_favourites:
                        fragment=new Favourites();
                        loadfragment(fragment);
                        break;

                    case R.id.navigation_shoppinglist:
                        fragment=new Shoppinglist();
                        loadfragment(fragment);
                        break;

                    case R.id.navigation_pantry:
                        fragment=new Pantry();
                        loadfragment(fragment);
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

//        Fragment frag= getSupportFragmentManager().findFragmentById(R.id.)
//
//        Log.d("msgtag","Fragment is"+frag);
//        Fragment f1= new SelectedItem();
//        Fragment f2= new Pantry();
//        Log.d("msgtag","inside checkfrag");
//        Log.d("msgtag","Fragment size is:"+frag.size());
//
//        Log.d("msgtag","valus of i is:"+i);
//        Fragment cur_frag= frag.get(i);
//        if(cur_frag==f1){
//            Log.d("msgtag","inside if");
//            loadfragment(f2);
//            tv1.setText("MY PRY");
//            tv2.setVisibility(View.VISIBLE);
//        }
//        else if(cur_frag==f2){
//            Log.d("msgtag","inside else");
//            loadfragment(f1);
//            tv1.setText("+ADD MORE RECIPES");
//            tv2.setVisibility(View.GONE);
//        }
    }
}