package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Email_verification extends AppCompatActivity {

    Button sendemailbtn,homebtn;
    private Handler mhandler=new Handler();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private SharedPreferences homepage;
    private  static  final  String sharedprefmsg="myprefsfile";

    protected void onStart() {
        super.onStart();
        mtoastRunnable.run();
    }


    private final Runnable mtoastRunnable = new Runnable() {
        @Override
        public void run() {
            int SPLASH_SCREEN_TIME_OUT=2000;
            Log.d("msgtag","Name is:"+firebaseAuth.getCurrentUser().getEmail());
            Log.d("msgtag","Email status is:"+firebaseAuth.getCurrentUser().isEmailVerified());
            firebaseAuth.getCurrentUser().reload();
            Log.d("msgtag","email is:"+firebaseAuth.getCurrentUser());

            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                Log.d("msgtag","hello from runnable");
                startActivity(new Intent(getApplicationContext(), Ingredients.class));
                mhandler.removeCallbacks(mtoastRunnable);
            }
            else{
                Log.d("msgtag","hello from runnable else");
                mhandler.postDelayed(this,SPLASH_SCREEN_TIME_OUT);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        sendemailbtn=findViewById(R.id.resendmail);
        homebtn=findViewById(R.id.homepagebtn);

        Log.d("msgtag","hello from emailverification");

        sendemailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("msgtag","Email Sucessfully Sent");
                            Toast.makeText(getApplicationContext(), "Email Sent For Verification", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.d("msgtag","Email not sent");
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("msgtag","Email Not sent"+e.toString());
                        Toast.makeText(getApplicationContext(), "Email Not Sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                homepage=getSharedPreferences(sharedprefmsg,0);
                SharedPreferences.Editor editor=homepage.edit();
                editor.putString("loginmsg","fromemailverification");
                editor.commit();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("msgtag","Destroyed method");
        mhandler.removeCallbacks(mtoastRunnable);
    }

}