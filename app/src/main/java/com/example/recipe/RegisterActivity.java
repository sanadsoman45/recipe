package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;

import android.util.Log;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.FirebaseDatabase;


import Model.Dataholder;

public class RegisterActivity extends AppCompatActivity {
    EditText emailText,passwordText,repasswordText;
    String email,pass,repass;
    String emailPattern="[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"+"\\@"+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+"("+"\\."+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+")+";
    String passwordPattern="^" +
            "(?=.*[0-9])" + //at least 1 digit
            "(?=.*[a-z])" +  // at least 1 lower case letter
            "(?=.*[A-Z])" +   // at least 1 upper case letter
            "(?=.*[@#$%^&+=])" +   // at least 1 special character
            "(?=\\S+$)" +   // no white spaces
            ".{6,}" +   // at least 6 characters
            "$";
    ImageView eyeimg1,eyeimg2;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        eyeimg1=findViewById(R.id.eyeimg1);
        eyeimg2=findViewById(R.id.eyeimg2);
        eyeimg1.setVisibility(View.INVISIBLE);
        eyeimg2.setVisibility(View.INVISIBLE);
        emailText=findViewById(R.id.emailText);
        passwordText=findViewById(R.id.passwordText);
        repasswordText=findViewById(R.id.repassword);
        Button signupButton = findViewById(R.id.registerbtn);
        Button cancelButton=findViewById(R.id.cancelButton);
        firebaseAuth= FirebaseAuth.getInstance();


        //Normal SignIn Logic
        signupButton.setOnClickListener(v -> {
            email=emailText.getText().toString().trim();
            pass=passwordText.getText().toString().trim();
            repass=repasswordText.getText().toString().trim();
            if(email.isEmpty())
            {

                Toast.makeText(RegisterActivity.this, "Email can't be empty", Toast.LENGTH_SHORT).show();
            }
            else if(!email.matches(emailPattern))
            {
                Toast.makeText(RegisterActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
            else if(pass.isEmpty())
            {
                Toast.makeText(this, "Password can't be empty", Toast.LENGTH_SHORT).show();
            }
            else if(!pass.matches(passwordPattern))
            {
                Toast.makeText(this, "Password must contain special character,Lower and upper case alphabets,digits and should be of length 6 to 11", Toast.LENGTH_SHORT).show();
            }
            else if(!pass.matches(repass))
            {
                Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();
            }

            else {
                if(firebaseAuth.getCurrentUser() != null){
                    Log.d("msgtag","Email is:"+firebaseAuth.getCurrentUser().getEmail());
                    if(firebaseAuth.getCurrentUser().isAnonymous()){
                        AuthCredential credential= EmailAuthProvider.getCredential(email,pass);
                        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    emailText.setText("");
                                    passwordText.setText("");
                                    repasswordText.setText("");
                                    Dataholder register = new Dataholder(email);
                                    FirebaseDatabase.getInstance().getReference("Super Chef").child("User Registration").child(firebaseAuth.getCurrentUser().getUid()).setValue(register);
                                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    emailText.setText("");
                                    passwordText.setText("");
                                    repasswordText.setText("");
                                    Log.d("User Is:","User Is:"+firebaseAuth.getCurrentUser());
                                    Log.d("User Is:","User is:"+firebaseUser);
                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d("msg","Email Sucessfully Sent");
                                            Toast.makeText(RegisterActivity.this, "Email Sent For Verification", Toast.LENGTH_SHORT).show();
                                            Log.d("msg","Exiting email id");
                                            startActivity(new Intent(getApplicationContext(), LoginPage.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("msg","Email Not sent");
                                            Toast.makeText(RegisterActivity.this, "Email Not Sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                                else
                                {
                                    if(task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                        Toast.makeText(getApplicationContext(),"The email address is already in use by another account.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("errormsg","Exception is:"+e);
                                Toast.makeText(RegisterActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(task -> {
                                Log.d("msg","Register Aajaa");
                                if (task.isSuccessful()) {
                                    Log.d("msg","Task is sucessful from registration");
                                    //send verification code
                                    emailText.setText("");
                                    passwordText.setText("");
                                    repasswordText.setText("");
                                    Dataholder register = new Dataholder(email);
                                    FirebaseDatabase.getInstance().getReference("Super Chef").child("User Registration").child(firebaseAuth.getCurrentUser().getUid()).setValue(register);
                                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    emailText.setText("");
                                    passwordText.setText("");
                                    repasswordText.setText("");
                                    Log.d("User Is:","User Is:"+firebaseAuth.getCurrentUser());
                                    Log.d("User Is:","User is:"+firebaseUser);
                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d("msg","Email Sucessfully Sent");
                                            Toast.makeText(RegisterActivity.this, "Email Sent For Verification", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), LoginPage.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("msg","Email Not sent");
                                            Toast.makeText(RegisterActivity.this, "Email Not Sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Log.d("msg","Exiting email id");

                                }
                                else
                                {
                                    if(task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                        Toast.makeText(getApplicationContext(),"The email address is already in use by another account.",Toast.LENGTH_SHORT).show();
                                    }

//                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this,e.getMessage(), Toast.LENGTH_LONG).show());
                }

            }
        });



        //Cancel Button Logic
        cancelButton.setOnClickListener(v -> {
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        });

        //reenterpassword gettext logic
        repasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //to check whether text is empty or not
                if(!repasswordText.getText().toString().isEmpty())
                {
                    //text is password type
                    if (repasswordText.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                        eyeimg2.setImageResource(R.drawable.ic_baseline_visibility_24);
                    }
                    else
                    {
                        //text is Text type
                        eyeimg2.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    }
                    //after all logic execution the eyeimage1 will be made visible
                    eyeimg2.setVisibility(View.VISIBLE);
                }
                else
                {
                    //if nologic is satisfied then view is invisible or text is empty
                    eyeimg2.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //check whether the repasswordtext has gained or lost focus
        repasswordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) //if true
                {
                    if(repasswordText.getText().toString().isEmpty())
                    {
                        //setting the repassword text to password
                        repasswordText.setTransformationMethod(new PasswordTransformationMethod());
                    }

                    if(!repasswordText.getText().toString().isEmpty())
                    {
                        //text is password type
                        if (repasswordText.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                            Log.d("msg","Password");
                            eyeimg2.setImageResource(R.drawable.ic_baseline_visibility_24);
                        }
                        else {
                            //text is text type
                            Log.d("msg","text Type");
                            eyeimg2.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                        }
                        eyeimg2.setVisibility(View.VISIBLE);
                    }
                }
                else //if false or doesnt have focus or focus out
                {
                    //Setting Visibility to Invisible
                    eyeimg2.setVisibility(View.INVISIBLE);
                }

            }
        });

        //logic for onclick on eyeimage2 imageview
        eyeimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //text is password type
                if (repasswordText.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                    repasswordText.setTransformationMethod(new SingleLineTransformationMethod());
                    eyeimg2.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }
                else {
                    //text is Texttype
                    repasswordText.setTransformationMethod(new PasswordTransformationMethod());
                    eyeimg2.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
                //for setting the cursor position after the length of the text
                repasswordText.setSelection(repasswordText.getText().length());
            }
        });

        //Password Field getText
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!passwordText.getText().toString().isEmpty())
                {
                    //text is password type
                    if (passwordText.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                        eyeimg1.setImageResource(R.drawable.ic_baseline_visibility_24);
                    }
                    else
                    {
                        //text is Text type
                        eyeimg1.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    }
                    //after all logic execution the eyeimage1 will be made visible
                    eyeimg1.setVisibility(View.VISIBLE);
                }
                else
                {
                    //if nologic is satisfied then view is invisible or text is empty
                    eyeimg1.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //show and hide eye icon on focus change
        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //if focus is gained has focus is true
                if(hasFocus) //if true
                {
                    if(passwordText.getText().toString().isEmpty())
                    {
                        passwordText.setTransformationMethod(new PasswordTransformationMethod());
                    }
                    if(!passwordText.getText().toString().isEmpty())
                    {
                        //text is password type
                        if (passwordText.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                            eyeimg1.setImageResource(R.drawable.ic_baseline_visibility_24);
                        }
                        else {
                            //text is text type
                            eyeimg1.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                        }
                        eyeimg1.setVisibility(View.VISIBLE);

                    }
                }
                else //if false or doesnt have focus or focus out
                {
                    eyeimg1.setVisibility(View.INVISIBLE);
                }

            }
        });

        //onclick on eyeimage with id eyeimage1
        eyeimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //text is password type
                if (passwordText.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                    passwordText.setTransformationMethod(new SingleLineTransformationMethod());
                    eyeimg1.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }
                else {
                    //text is Texttype
                    passwordText.setTransformationMethod(new PasswordTransformationMethod());
                    eyeimg1.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
                //for setting the cursor position after the length of the text
                passwordText.setSelection(passwordText.getText().length());
            }
        });
    }

}