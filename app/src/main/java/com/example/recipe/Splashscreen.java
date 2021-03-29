package com.example.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Splashscreen extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=5000;
    private ImageView imgview;
    private  static  final  String sharedprefmsg="myprefsfile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        imgview=findViewById(R.id.textViewEventImage);
        Glide.with(getApplicationContext()).asGif().load(R.raw.superlogo).into(imgview);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(Splashscreen.this,
                        MainActivity.class);

                startActivity(i);

                finish();

            }
        }, SPLASH_SCREEN_TIME_OUT);


    }
}