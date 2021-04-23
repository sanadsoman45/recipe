package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class resetpassword_1 extends AppCompatActivity {

    String pass;
    EditText passwordfield,emailtext,emailtext1;
    Button verifypassbtn;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    Button changeemailbtn;
    ImageView eyeimg1,eyeimg2,eyeimg3;
    LinearLayout passwordcheck,update_email;
    String passwordPattern="^" +
            "(?=.*[0-9])" + //at least 1 digit
            "(?=.*[a-z])" +  // at least 1 lower case letter
            "(?=.*[A-Z])" +   // at least 1 upper case letter
            "(?=.*[@#$%^&+=])" +   // at least 1 special character
            "(?=\\S+$)" +   // no white spaces
            ".{6,}" +   // at least 6 characters
            "$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword_1);
        passwordfield=findViewById(R.id.passwordfield);
        verifypassbtn=findViewById(R.id.verifypassbtn);
        passwordcheck=findViewById(R.id.passwordcheck);
        update_email=findViewById(R.id.update_email);
        fauth=FirebaseAuth.getInstance();
        fuser=fauth.getCurrentUser();
        changeemailbtn=findViewById(R.id.changeemailbtn);
        emailtext=findViewById(R.id.emailtext);
        emailtext1=findViewById(R.id.emailtext1);
        eyeimg1=findViewById(R.id.eyeimg1);
        eyeimg2=findViewById(R.id.eyeimg2);
        eyeimg3=findViewById(R.id.eyeimg3);
        eyeimg1.setVisibility(View.GONE);
        eyeimg2.setVisibility(View.GONE);
        eyeimg3.setVisibility(View.GONE);

        verifypassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass=passwordfield.getText().toString().trim();
                if(pass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Password can't be empty", Toast.LENGTH_SHORT).show();
                }

                else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), pass);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                passwordfield.setText("");
                                passwordcheck.setVisibility(View.GONE);
                                update_email.setVisibility(View.VISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error is:"+e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        changeemailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=emailtext.getText().toString().trim();
                String repass=emailtext1.getText().toString().trim();
                if(password.isEmpty()){
                    Toast.makeText(resetpassword_1.this, "Password Is Empty", Toast.LENGTH_SHORT).show();
                }
                else if(!password.matches(passwordPattern)){
                    Toast.makeText(getApplicationContext(), "Password must contain special character,Lower and upper case alphabets,digits and should be of length 6 to 11", Toast.LENGTH_SHORT).show();
                }
                else if(repass.isEmpty()){
                    Toast.makeText(resetpassword_1.this, "Renter Password Is Empty", Toast.LENGTH_SHORT).show();
                }
                else if(!repass.equals(password)){
                    Toast.makeText(resetpassword_1.this, "Reenter Password Doesnt match Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    fuser.updatePassword(password);
                    Toast.makeText(resetpassword_1.this, "Password Is Updated Sucessfully", Toast.LENGTH_SHORT).show();
                    fauth.signOut();
                    startActivity(new Intent(getApplicationContext(),LoginPage.class));
                }
            }
        });

        eyeimg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailtext1.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                    emailtext1.setTransformationMethod(new SingleLineTransformationMethod());
                    eyeimg3.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }
                else {
                    //text is Texttype
                    emailtext1.setTransformationMethod(new PasswordTransformationMethod());
                    eyeimg3.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
                //for setting the cursor position after the length of the text
                emailtext1.setSelection(emailtext.getText().length());
            }
        });

        eyeimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailtext.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                    emailtext.setTransformationMethod(new SingleLineTransformationMethod());
                    eyeimg2.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }
                else {
                    //text is Texttype
                    emailtext.setTransformationMethod(new PasswordTransformationMethod());
                    eyeimg2.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
                //for setting the cursor position after the length of the text
                emailtext.setSelection(emailtext.getText().length());
            }
        });

        eyeimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //text is password type
                if (passwordfield.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                    passwordfield.setTransformationMethod(new SingleLineTransformationMethod());
                    eyeimg1.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }
                else {
                    //text is Texttype
                    passwordfield.setTransformationMethod(new PasswordTransformationMethod());
                    eyeimg1.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
                //for setting the cursor position after the length of the text
                passwordfield.setSelection(passwordfield.getText().length());
            }
        });

        passwordfield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!passwordfield.getText().toString().isEmpty())
                {
                    //text is password type
                    if (passwordfield.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
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
        passwordfield.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //if focus is gained has focus is true
                if(hasFocus) //if true
                {
                    if(passwordfield.getText().toString().isEmpty())
                    {
                        passwordfield.setTransformationMethod(new PasswordTransformationMethod());
                    }
                    if(!passwordfield.getText().toString().isEmpty())
                    {
                        //text is password type
                        if (passwordfield.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
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

        emailtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!emailtext.getText().toString().isEmpty())
                {
                    //text is password type
                    if (emailtext.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
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



        //show and hide eye icon on focus change
        emailtext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //if focus is gained has focus is true
                if(hasFocus) //if true
                {
                    if(emailtext.getText().toString().isEmpty())
                    {
                        emailtext.setTransformationMethod(new PasswordTransformationMethod());
                    }
                    if(!emailtext.getText().toString().isEmpty())
                    {
                        //text is password type
                        if (emailtext.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                            eyeimg2.setImageResource(R.drawable.ic_baseline_visibility_24);
                        }
                        else {
                            //text is text type
                            eyeimg2.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                        }
                        eyeimg2.setVisibility(View.VISIBLE);

                    }
                }
                else //if false or doesnt have focus or focus out
                {
                    eyeimg2.setVisibility(View.INVISIBLE);
                }

            }
        });

        emailtext1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!emailtext1.getText().toString().isEmpty())
                {
                    //text is password type
                    if (emailtext1.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                        eyeimg3.setImageResource(R.drawable.ic_baseline_visibility_24);
                    }
                    else
                    {
                        //text is Text type
                        eyeimg3.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    }
                    //after all logic execution the eyeimage1 will be made visible
                    eyeimg3.setVisibility(View.VISIBLE);
                }
                else
                {
                    //if nologic is satisfied then view is invisible or text is empty
                    eyeimg3.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //show and hide eye icon on focus change
        emailtext1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //if focus is gained has focus is true
                if(hasFocus) //if true
                {
                    if(emailtext1.getText().toString().isEmpty())
                    {
                        emailtext1.setTransformationMethod(new PasswordTransformationMethod());
                    }
                    if(!emailtext1.getText().toString().isEmpty())
                    {
                        //text is password type
                        if (emailtext.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                            eyeimg3.setImageResource(R.drawable.ic_baseline_visibility_24);
                        }
                        else {
                            //text is text type
                            eyeimg3.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                        }
                        eyeimg3.setVisibility(View.VISIBLE);

                    }
                }
                else //if false or doesnt have focus or focus out
                {
                    eyeimg3.setVisibility(View.INVISIBLE);
                }

            }
        });

    }
}