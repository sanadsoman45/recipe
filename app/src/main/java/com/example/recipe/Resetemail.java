package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Resetemail extends AppCompatActivity {
    EditText passwordText,emailadd_text;
    LinearLayout updateemail,passwordcheck;
    String pass,email;
    String emailPattern="[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"+"\\@"+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+"("+"\\."+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+")+";

    ImageView eyeimg1;
    Button check_pass,update_email;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_resetemail);
        passwordcheck=findViewById(R.id.passwordcheck);
        updateemail=findViewById(R.id.update_email);
        eyeimg1=findViewById(R.id.eyeimg1);
        eyeimg1.setVisibility(View.INVISIBLE);
        passwordText=findViewById(R.id.passwordfield);
        check_pass=findViewById(R.id.verifypassbtn);
        update_email=findViewById(R.id.changeemailbtn);
        emailadd_text=findViewById(R.id.emailtext);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();

        check_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass=passwordText.getText().toString().trim();
                if(pass.isEmpty())
                {
                    Toast.makeText(Resetemail.this, "Password can't be empty", Toast.LENGTH_SHORT).show();
                }

                else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), pass);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                passwordText.setText("");
                                passwordcheck.setVisibility(View.GONE);
                                updateemail.setVisibility(View.VISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Resetemail.this, "Error is:"+e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailadd_text.getText().toString().trim();
                if(email.isEmpty())
                {
                    Toast.makeText(Resetemail.this, "Email can't be empty", Toast.LENGTH_SHORT).show();
                }
                else if(!email.matches(emailPattern))
                {
                    Toast.makeText(Resetemail.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                }
                else{

                    if(email.equals(firebaseUser.getEmail())){
                        Log.d("msgt","In if "+firebaseUser.getEmail());
                        Toast.makeText(Resetemail.this, "Same Email Can't be Used", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.d("msgt","In else "+firebaseUser.getEmail());
                        firebaseUser.updateEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            emailadd_text.setText("");
                                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d("msg","Email Sucessfully Sent");
                                                    Toast.makeText(Resetemail.this, "Email Sent For Verification", Toast.LENGTH_SHORT).show();
                                                    Log.d("msg","Exiting email id");
                                                    Toast.makeText(Resetemail.this, "User Email is Updated Succesfully", Toast.LENGTH_SHORT).show();
                                                    firebaseAuth.signOut();
                                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("msg","Email Not sent");
                                                    Toast.makeText(Resetemail.this, "Email Not Sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            Log.d("msgtag", "User email address updated.");
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Resetemail.this, "Error is:"+e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        });

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



    }
}