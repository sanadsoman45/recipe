package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {
    private Button cancelbtn,signinbtn;
    private TextView forgotPassword;
    private FirebaseAuth mAuth;
    private ImageView eyeimg1;
    private CheckBox rembmecb;
    private FirebaseAuth fauth;
    private FirebaseUser fuser;
    private SharedPreferences loginrembme;
    private  static  final  String sharedprefmsg="myprefsfile";
    private EditText useremail,password;
    private String usermail,userpass;
    private String emailPattern="[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"+"\\@"+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+"("+"\\."+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+")+";
    private String passwordPattern="^" +
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_page);
        useremail=findViewById(R.id.emailfield);
        password=findViewById(R.id.passwordfield);
        signinbtn=findViewById(R.id.signin);
        cancelbtn=findViewById(R.id.cancelbtn);
        eyeimg1=findViewById(R.id.eyeimg1);
        eyeimg1.setVisibility(View.INVISIBLE);
        fauth=FirebaseAuth.getInstance();
        fuser=fauth.getCurrentUser();
        rembmecb=findViewById(R.id.Rememberme);
        forgotPassword=findViewById(R.id.forgotpassword);
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userpass=password.getText().toString().trim();
                usermail=useremail.getText().toString().trim();
                if(usermail.isEmpty())
                {
                    useremail.setError("Email Id Cannot Be Empty");
                }
                else if(!usermail.matches(emailPattern))
                {
                    useremail.setError("Invalid Email Id");
                }
                else if(userpass.isEmpty())
                {
                    password.setError("Password Is Empty");
                }
                else if(!userpass.matches(passwordPattern))
                {
                    password.setError("Invalid Password");
                }
                else
                {
                    try {
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signInWithEmailAndPassword(usermail,userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(LoginPage.this, "Hello ji from login page", Toast.LENGTH_SHORT).show();
                                    Log.d("msg","Hello from login");
                                    useremail.setText("");
                                    password.setText("");
                                    if(rembmecb.isChecked())
                                    {
                                        loginrembme=getSharedPreferences(sharedprefmsg,0);
                                        SharedPreferences.Editor editor=loginrembme.edit();
                                        editor.putString("message","rememberme");
                                        editor.commit();
                                    }
                                    Intent i=new Intent(getApplicationContext(), Ingredients.class);
                                    i.putExtra("email",mAuth.getCurrentUser().getEmail());
                                    i.putExtra("userid",mAuth.getCurrentUser().getUid());
                                    startActivity(i);

                                }
                                else
                                {
                                    useremail.setText("");
                                    password.setText("");
                                    Toast.makeText(LoginPage.this, "Invalid Email Or Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginPage.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    catch (Exception e)
                    {
                        Log.d("msg",e.toString());
                    }
                }
            }
        });

    cancelbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    });
    forgotPassword.setOnClickListener(v -> {
        Intent intent=new Intent(getApplicationContext(),ForgotPassword.class);
        startActivity(intent);
    });

    password.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!password.getText().toString().isEmpty())
            {
                //text is password type
                if (password.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
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
    password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //if focus is gained has focus is true
            if(hasFocus) //if true
            {
                if(password.getText().toString().isEmpty())
                {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
                if(!password.getText().toString().isEmpty())
                {
                    //text is password type
                    if (password.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
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
            if (password.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                password.setTransformationMethod(new SingleLineTransformationMethod());
                eyeimg1.setImageResource(R.drawable.ic_baseline_visibility_off_24);
            }
            else {
                //text is Texttype
                password.setTransformationMethod(new PasswordTransformationMethod());
                eyeimg1.setImageResource(R.drawable.ic_baseline_visibility_24);
            }
            //for setting the cursor position after the length of the text
            password.setSelection(password.getText().length());
        }
    });

    }
}