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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import Adapter.DatabaseHandler;
import Adapter.MyAdapter;
import Utils.Util;

public class Ingredients extends AppCompatActivity implements fragmenttoactivity, fragtoactivityshoppinglist, GoogleApiClient.OnConnectionFailedListener {
    TextView counttv,butcounttv,counttexttv,ing_header;
    FrameLayout fl;
    RelativeLayout pantry_lay;
    EditText ing_search,search_relative;
    Button backbtn;
    ImageButton settings_btn,settings_btn1;
    AppBarLayout appbarlay;
    int int_flag=0;
    RelativeLayout rlv,pantryfragrellay;
    Toolbar toolbar;
    private SharedPreferences forgotpassword;
    NestedScrollView nsv;
    CollapsingToolbarLayout collapsetoolbar;
    private DatabaseHandler dbh;
    String frag_name;
    TextView tv1,tv2,seerecipe;
    LinearLayout linearlay;
    SharedPreferences shpmsgvar,shpmsgret;
    int count=0;
    ImageButton user_icon,user_icon1;
    private  static  final  String sharedprefmsg="myprefsfile";
    private GoogleApiClient mGoogleApiClient;
    FirebaseUser firebaseUser;
    private SharedPreferences search_frag_check;
    private SharedPreferences anonymoususer_check;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        Log.d("msgtag","list is:"+firebaseAuth.getCurrentUser().getProviderData());
        Log.d("msgtag","email is: "+firebaseAuth.getCurrentUser().getEmail());
//        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(this);
//        Log.d("msghello","account is:"+account.getEmail());
        if(!firebaseAuth.getCurrentUser().isEmailVerified() & firebaseAuth.getCurrentUser()!=null & !firebaseAuth.getCurrentUser().isAnonymous()){
            Log.d("msgtag","email verification page");
            startActivity(new Intent(getApplicationContext(), Email_verification.class));
        }
    }

    public void check_search_frag(String Fragment_name){
        if(Fragment_name=="pantry"){
            search_frag_check=getSharedPreferences(sharedprefmsg,0);
            SharedPreferences.Editor editor=search_frag_check.edit();
            editor.putString("fragment_name","pantry");
            editor.commit();
            startActivity(new Intent(getApplicationContext(),search_fragment.class));
        }
        else if(Fragment_name=="shoppinglist"){
            search_frag_check=getSharedPreferences(sharedprefmsg,0);
            SharedPreferences.Editor editor=search_frag_check.edit();
            editor.putString("fragment_name","shoppinglist");
            editor.commit();
            startActivity(new Intent(getApplicationContext(),search_fragment.class));
        }
        else if(Fragment_name=="favourites"){
            startActivity(new Intent(getApplicationContext(),Searchactivity_favourites.class));
        }
        else if(Fragment_name=="menu"){
            startActivity(new Intent(getApplicationContext(),firebasesearchactivity.class));
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadfragment(new Pantry());
        String i=getIntent().getStringExtra("fragname");
        Log.d("msgintent","Intent is:"+i);
        setContentView(R.layout.activity_ingredients);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ing_header=findViewById(R.id.mying);
        nsv=findViewById(R.id.nestedScrollView);
        counttv=findViewById(R.id.counttv);
        collapsetoolbar=findViewById(R.id.collapse);
        backbtn=findViewById(R.id.ing_col_user_pantry);
        user_icon=findViewById(R.id.ing_col_user);
        user_icon1=findViewById(R.id.ing_user);
        settings_btn=findViewById(R.id.ing_col_settings);
        settings_btn1=findViewById(R.id.ing_settings);
        pantryfragrellay=findViewById(R.id.pantry_frag_lay);
        butcounttv=findViewById(R.id.mypantrycount);
        dbh=new DatabaseHandler(getApplicationContext());
        linearlay=findViewById(R.id.bottommenulinear);
        appbarlay=findViewById(R.id.appbar);
        toolbar=findViewById(R.id.toolbar);
        fl=findViewById(R.id.flFragment);
        counttexttv=findViewById(R.id.counttv_pantry);
        fl.setForeground(null);
        ing_search=findViewById(R.id.ing_col_search);
        search_relative=findViewById(R.id.ing_search);
        shpmsgvar=getSharedPreferences(sharedprefmsg,0);
        SharedPreferences.Editor editor=shpmsgvar.edit();
        editor.putString("message","pantry");
        editor.commit();
        frag_name="pantry";
        rlv=findViewById(R.id.relativecollapse);
        tv1=findViewById(R.id.mypantrytext);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        tv2=findViewById(R.id.mypantrycount);
        seerecipe=findViewById(R.id.seerecipe);
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(this);


        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();

        if(i!=null){
            Log.d("msgintent","Intent is:"+i);
            if(i.equals("pantry")){
                startActivity(new Intent(getApplicationContext(),Ingredients.class));
                navigation.setSelectedItemId(R.id.navigation_pantry);
            }
            else if(i.equals("shoppinglist")){
                int_flag=1;
                counttv.setVisibility(View.VISIBLE);
                counttexttv.setVisibility(View.VISIBLE);
                settings_btn.setVisibility(View.VISIBLE);
                settings_btn1.setVisibility(View.VISIBLE);
                frag_name="shoppinglist";
                Log.d("fragname","fragname is:"+frag_name);
                ing_search.setHint("Add ingredients");
                search_relative.setHint("Add ingredients");
                collapsetoolbar.setVisibility(View.VISIBLE);
                pantryfragrellay.setVisibility(View.GONE);
                tv2.setVisibility(View.VISIBLE);
                ing_header.setText("Shopping List");
                Log.d("count","count is:"+count);
                counttv.setText("You have "+dbh.get_count_ingredients_cart(firebaseUser.getUid())+" items in the list");
                loadfragment(new Shoppinglist());
                linearlay.setVisibility(View.GONE);
                navigation.setSelectedItemId(R.id.navigation_shoppinglist);
            }
            else if(i.equals("favourites")){
                settings_btn.setVisibility(View.VISIBLE);
                settings_btn1.setVisibility(View.VISIBLE);
                counttexttv.setVisibility(View.GONE);
                navigation.setSelectedItemId(R.id.navigation_favourites);
                counttv.setVisibility(View.GONE);
                if(firebaseUser.isAnonymous()){
                    settings_btn.setVisibility(View.GONE);
                    settings_btn1.setVisibility(View.GONE);
                }
                int_flag=0;
                frag_name="favourites";
                Log.d("frag_name","Fragment name is:"+frag_name);
                ing_header.setText("FAVOURITES");
                Log.d("fragname","fragname is:"+frag_name);
                loadfragment(new Favourites());
                linearlay.setVisibility(View.GONE);
            }
            else if(i.equals("menu")){
                navigation.setSelectedItemId(R.id.navigation_Menu);
                collapsetoolbar.setVisibility(View.VISIBLE);
                pantryfragrellay.setVisibility(View.GONE);
                settings_btn.setVisibility(View.GONE);
                frag_name="menu";
                settings_btn1.setVisibility(View.GONE);
                ing_header.setText("SUPERCOOK");
                settings_btn.setVisibility(View.GONE);
                settings_btn1.setVisibility(View.GONE);
                counttexttv.setVisibility(View.GONE);
                counttv.setVisibility(View.GONE);
                int_flag=0;
                loadfragment(new Menu());
                linearlay.setVisibility(View.GONE);
            }
        }





        search_relative.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("fragname","fragname is:"+frag_name);
                check_search_frag(frag_name);
                return false;
            }
        });

        ing_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("fragname","fragname is:"+frag_name);
                check_search_frag(frag_name);
                return false;
            }
        });


        seerecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.setSelectedItemId(R.id.navigation_Menu);
                loadfragment(new Menu());
                collapsetoolbar.setVisibility(View.VISIBLE);
                linearlay.setVisibility(View.GONE);
                ing_header.setText("SUPERCOOK");
                settings_btn.setVisibility(View.GONE);
                settings_btn1.setVisibility(View.GONE);
                counttexttv.setVisibility(View.GONE);
                counttv.setVisibility(View.GONE);
                pantryfragrellay.setVisibility(View.GONE);
            }
        });

        user_icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("usertag","Inside click");
                firebaseUser= firebaseAuth.getCurrentUser();
                if(firebaseUser.isAnonymous()){
                    anonymoususer_check=getSharedPreferences(sharedprefmsg,0);
                    SharedPreferences.Editor editor=anonymoususer_check.edit();
                    editor.putString("user_check_any","yes");
                    editor.commit();
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Logout Successfully! From Anonymous", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }

                else{
                    Toast.makeText(Ingredients.this, "LOGGED IN AS:"+ firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                    Log.d("msgtag","else from ingredients");
                    if(account !=null){
                        google_user_settings(v,account);
                    }
                    else{
                        user_settings_popup(v);
                    }
                }
            }
        });

        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("usertag1","Inside click");
                firebaseUser= firebaseAuth.getCurrentUser();
                if(firebaseUser.isAnonymous()){
                    anonymoususer_check=getSharedPreferences(sharedprefmsg,0);
                    SharedPreferences.Editor editor=anonymoususer_check.edit();
                    editor.putString("user_check_any","yes");
                    editor.commit();
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Logout Successfully! From Anonymous", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }

                else{
                    Toast.makeText(Ingredients.this, "LOGGED IN AS:"+ firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                    Log.d("msgtag","else from ingredients");
                    if(account !=null){
                        google_user_settings(v,account);
                    }
                    else{
                        user_settings_popup(v);
                    }
                }
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        
        appbarlay.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    rlv.setVisibility(View.VISIBLE);
                    rlv.setBackgroundColor(Color.parseColor("#D7456A"));
                    toolbar.setBackgroundColor(Color.parseColor("#D7456A"));
                    toolbar.setVisibility(View.VISIBLE);
                } else {
                    toolbar.setVisibility(View.GONE);
                    rlv.setVisibility(View.GONE);
                    rlv.setBackgroundColor(0);
                    toolbar.setBackgroundColor(0);
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId())
                {

                    case R.id.navigation_Menu:
                        collapsetoolbar.setVisibility(View.VISIBLE);
                        pantryfragrellay.setVisibility(View.GONE);
                        settings_btn.setVisibility(View.GONE);
                        frag_name="menu";
                        settings_btn1.setVisibility(View.GONE);
                        ing_header.setText("SUPERCHEF");
                        settings_btn.setVisibility(View.GONE);
                        settings_btn1.setVisibility(View.GONE);
                        counttexttv.setVisibility(View.GONE);
                        counttv.setVisibility(View.GONE);
                        int_flag=0;
                        fragment=new Menu();
                        loadfragment(fragment);
                        linearlay.setVisibility(View.GONE);
                        break;

                    case R.id.navigation_favourites:
                        settings_btn.setVisibility(View.VISIBLE);
                        settings_btn1.setVisibility(View.VISIBLE);
                        counttexttv.setVisibility(View.GONE);
                        counttv.setVisibility(View.GONE);
                        if(firebaseUser.isAnonymous()){
                            settings_btn.setVisibility(View.GONE);
                            settings_btn1.setVisibility(View.GONE);
                        }
                        int_flag=0;
                        frag_name="favourites";
                        Log.d("frag_name","Fragment name is:"+frag_name);
                        ing_header.setText("FAVOURITES");
                        Log.d("fragname","fragname is:"+frag_name);
                        fragment=new Favourites();
                        loadfragment(fragment);
                        linearlay.setVisibility(View.GONE);
                        break;

                    case R.id.navigation_shoppinglist:
                        int_flag=1;
                        counttv.setVisibility(View.VISIBLE);
                        counttexttv.setVisibility(View.VISIBLE);
                        settings_btn.setVisibility(View.VISIBLE);
                        settings_btn1.setVisibility(View.VISIBLE);
                        frag_name="shoppinglist";
                        Log.d("fragname","fragname is:"+frag_name);
                        fragment=new Shoppinglist();
                        ing_search.setHint("Add ingredients");
                        search_relative.setHint("Add ingredients");
                        collapsetoolbar.setVisibility(View.VISIBLE);
                        pantryfragrellay.setVisibility(View.GONE);
                        tv2.setVisibility(View.VISIBLE);
                        ing_header.setText("Shopping List");
                        Log.d("count","count is:"+count);
                        counttv.setText("You have "+dbh.get_count_ingredients_cart(firebaseUser.getUid())+" items in the list");
                        loadfragment(fragment);
                        linearlay.setVisibility(View.GONE);
                        break;

                    case R.id.navigation_pantry:
                        int_flag=0;
                        counttv.setVisibility(View.VISIBLE);
                        counttexttv.setVisibility(View.VISIBLE);
                        settings_btn.setVisibility(View.VISIBLE);
                        settings_btn1.setVisibility(View.VISIBLE);
                        frag_name="pantry";
                        Log.d("fragname","fragname is:"+frag_name);
                        ing_search.setHint("Add/remove/paste ingredients");
                        search_relative.setHint("Add ingredient");
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            checkfrag(getWindow().getDecorView().getRootView());
                        }
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            checkPantry(getWindow().getDecorView().getRootView());
                        }
                        fragment=new Pantry();
                        ing_header.setText("My ingredients");
                        loadfragment(fragment);
                        linearlay.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        shpmsgret=getSharedPreferences(sharedprefmsg,0);
        if(shpmsgret.contains("message")) {
            String message = shpmsgret.getString("message", "notfound");
            if(message.equals("pantry")){
                finishAffinity();
            }
            else{
                finish();
            }
        }


    }

    @SuppressLint("RestrictedApi")
    public void google_user_settings(View v,GoogleSignInAccount account){
        @SuppressLint("RestrictedApi") MenuBuilder menuBuilder =new MenuBuilder(getApplicationContext());
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.google_user_setting,menuBuilder);
        @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(this, menuBuilder, v);
        optionsMenu.setForceShowIcon(true);
        optionsMenu.show();
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                if(item.getItemId()==R.id.google_signout){
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            FirebaseAuth.getInstance().signOut();
                            Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                            Log.d("errormsg","from Ingredients Curreuser"+account.getEmail());
                            Toast.makeText(getApplicationContext(), "Logout Successfully! From Google", Toast.LENGTH_SHORT).show();
                            startActivity(i1);
                        }
                    });
                }
                return true;
            }

            @Override
            public void onMenuModeChange(@NonNull MenuBuilder menu) {

            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void user_settings_popup(View v){
        @SuppressLint("RestrictedApi") MenuBuilder menuBuilder =new MenuBuilder(getApplicationContext());
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.user_settings,menuBuilder);
        @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(this, menuBuilder, v);
        optionsMenu.setForceShowIcon(true);
        optionsMenu.show();
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                if(item.getItemId()==R.id.change_email){
                    startActivity(new Intent(getApplicationContext(),Resetemail.class));
                }
                else if(item.getItemId()==R.id.change_password){
//                    startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
//                    forgotpassword=getSharedPreferences(sharedprefmsg,0);
//                    SharedPreferences.Editor editor=forgotpassword.edit();
//                    editor.putString("forgotpassword","true");
//                    editor.commit();
                    startActivity(new Intent(getApplicationContext(),resetpassword_1.class));
                }
                else if(item.getItemId()==R.id.logout){
                    firebaseAuth.signOut();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                return true;
            }

            @Override
            public void onMenuModeChange(@NonNull MenuBuilder menu) {

            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void popup(View v){
        @SuppressLint("RestrictedApi") MenuBuilder menuBuilder =new MenuBuilder(getApplicationContext());
        MenuInflater inflater = new MenuInflater(this);

        if(frag_name.equals("pantry")){
            inflater.inflate(R.menu.menu_items, menuBuilder);
        }

        else if(frag_name.equals("favourites")){
            Log.d("favmsg","inside favourites");
            inflater.inflate(R.menu.favouritesmenu, menuBuilder);
        }


        else if(frag_name.equals("shoppinglist")){
            inflater.inflate(R.menu.shoppingcartmenu,menuBuilder);
        }

        @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(this, menuBuilder, v);
        optionsMenu.setForceShowIcon(true);
        optionsMenu.show();
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                if(item.getItemId()==R.id.action_settings){
                    if(dbh.get_count_ingredients(firebaseUser.getUid())>0){
                        dbh.deleteallingredients(firebaseUser.getUid());
                        butcounttv.setText(String.valueOf(dbh.get_count_ingredients(firebaseUser.getUid())));
                        counttv.setText("You Have "+dbh.get_count_ingredients(firebaseUser.getUid())+" Ingredients");
                        counttexttv.setText("You Have "+dbh.get_count_ingredients(firebaseUser.getUid())+" Ingredients");
                        Toast.makeText(Ingredients.this, "All ingredients deleted", Toast.LENGTH_SHORT).show();
                        shpmsgret=getSharedPreferences(sharedprefmsg,0);
                        Log.d("msgtag","message is:"+shpmsgret.contains("message"));
                        if(shpmsgret.contains("message"))
                        {
                            String message=shpmsgret.getString("message","notfound");
                            Log.d("msgtag",message);
                            Log.d("msg", String.valueOf(message=="pantry"));
                            if(message.equals("pantry")){
                                loadfragment(new Pantry());
                                ing_header.setText("My ingredients");
                                tv1.setText("My Pantry");
                                collapsetoolbar.setVisibility(View.VISIBLE);
                                pantryfragrellay.setVisibility(View.GONE);
                                tv2.setVisibility(View.VISIBLE);
                            }
                            else if(message.equals("selectedingredients")){
                                loadfragment(new SelectedItem());
                                collapsetoolbar.setVisibility(View.GONE);
                                pantryfragrellay.setVisibility(View.VISIBLE);
                                tv1.setText("+ADD MORE RECIPES");
                                tv2.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                else if(item.getItemId()==R.id.shopping_clear_all){
                    dbh.delete_purchased(firebaseUser.getUid());
                    counttv.setText("You have "+dbh.get_count_ingredients_cart(firebaseUser.getUid())+" items in the list");
                    loadfragment(new Shoppinglist());
                }
                else if(item.getItemId() == R.id.shopping_clear){
                    dbh.empty_cart(firebaseUser.getUid());
                    counttv.setText("You have "+dbh.get_count_ingredients_cart(firebaseUser.getUid())+" items in the list");
                    loadfragment(new Shoppinglist());
                }
                else if(item.getItemId() == R.id.fav_menu_item){
                    dbh.empty_fav(firebaseUser.getUid());
                    loadfragment(new Favourites());
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
        if(shpmsgret.contains("message"))
        {
            String message=shpmsgret.getString("message","notfound");
            if(message.equals("pantry")){
                SharedPreferences.Editor editor=shpmsgvar.edit();
                editor.putString("message","selectedingredients");
                loadfragment(new SelectedItem());
                collapsetoolbar.setVisibility(View.GONE);
                pantryfragrellay.setVisibility(View.VISIBLE);
                tv1.setText("+ADD MORE ITEMS");
                tv2.setVisibility(View.GONE);
                editor.commit();
            }
            else if(message.equals("selectedingredients")){
                SharedPreferences.Editor editor=shpmsgvar.edit();
                editor.putString("message","pantry");
                ing_header.setText("My ingredients");
                loadfragment(new Pantry());
                collapsetoolbar.setVisibility(View.VISIBLE);
                pantryfragrellay.setVisibility(View.GONE);
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
                ing_header.setText("My ingredients");
                loadfragment(new Pantry());
                collapsetoolbar.setVisibility(View.VISIBLE);
                pantryfragrellay.setVisibility(View.GONE);
                tv1.setText("My Pantry");
                appbarlay.setNestedScrollingEnabled(true);
                seerecipe.setBackgroundColor(Color.parseColor("#93C75B"));
                tv2.setVisibility(View.VISIBLE);
                editor.commit();
            }
        }
    }



    @Override
    public void communicate(int count) {
        butcounttv.setText(String.valueOf(count));
        counttexttv.setText("You Have "+count+" Ingredients");
        counttv.setText("You Have "+count+" Ingredients");
    }

    @Override
    public void communicate_shoppinglist(int count) {
        this.count=count;
        counttv.setText("You have "+count+" items in the list");
        Log.d("count","count from ingredients  is:"+this.count);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"Connection Failed",Toast.LENGTH_SHORT).show();
    }

}