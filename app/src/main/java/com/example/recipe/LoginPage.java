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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import Adapter.DatabaseHandler;

public class LoginPage extends AppCompatActivity {
    private Button cancelbtn,signinbtn;
    private TextView forgotPassword;
    private SignInButton googlesignbtn;
    private FirebaseAuth mAuth;
    private ImageView eyeimg1;
    private CheckBox rembmecb;
    private FirebaseAuth fauth;
    private int RC_SIGN_IN=101;
    private GoogleSignInClient mgooglesigninclient;
    private FirebaseUser fuser;
    private SharedPreferences loginrembme;
    private  static  final  String sharedprefmsg="myprefsfile";
    private EditText useremail,password;
    private String usermail,userpass;
    DatabaseHandler dbh;
    private String emailPattern="[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"+"\\@"+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+"("+"\\."+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+")+";
    private String passwordPattern="^" +
            "(?=.*[0-9])" + //at least 1 digit
            "(?=.*[a-z])" +  // at least 1 lower case letter
            "(?=.*[A-Z])" +   // at least 1 upper case letter
            "(?=.*[@#$%^&+=])" +   // at least 1 special character
            "(?=\\S+$)" +   // no white spaces
            ".{6,}" +   // at least 6 characters
            "$";
    FirebaseAuth firebaseauth=FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_page);
        processRequest();
        dbh=new DatabaseHandler(getApplicationContext());
        useremail=findViewById(R.id.emailfield);
        password=findViewById(R.id.passwordfield);
        signinbtn=findViewById(R.id.signin);
        googlesignbtn=findViewById(R.id.btn_google_signin);
        cancelbtn=findViewById(R.id.cancelbtn);
        eyeimg1=findViewById(R.id.eyeimg1);
        eyeimg1.setVisibility(View.INVISIBLE);
        fauth=FirebaseAuth.getInstance();
        fuser=fauth.getCurrentUser();
        rembmecb=findViewById(R.id.Rememberme);
        forgotPassword=findViewById(R.id.forgotpassword);

        googlesignbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processlogin();
            }
        });


        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userpass=password.getText().toString().trim();
                usermail=useremail.getText().toString().trim();
                if(usermail.isEmpty())
                {
                    Toast.makeText(LoginPage.this, "\"Email Id Cannot Be Empty\"", Toast.LENGTH_SHORT).show();
                }
                else if(!usermail.matches(emailPattern))
                {
                    Toast.makeText(LoginPage.this, "Invalid Email Id", Toast.LENGTH_SHORT).show();
                }
                else if(userpass.isEmpty())
                {
                    Toast.makeText(LoginPage.this, "Password Is Empty", Toast.LENGTH_SHORT).show();
                }
                else if(!userpass.matches(passwordPattern))
                {
                    Toast.makeText(LoginPage.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        mAuth = FirebaseAuth.getInstance();
                        if(mAuth.getCurrentUser()!=null){
                            if(fuser.isAnonymous()){
                                fauth.signOut();
                                dbh.deleteallingredients(fuser.getUid());
                                dbh.empty_fav(fuser.getUid());
                                dbh.empty_cart(fuser.getUid());
                                fuser.delete();
                            }
                        }

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
                                        Log.d("msgtag","rembme");
                                        loginrembme=getSharedPreferences(sharedprefmsg,0);
                                        SharedPreferences.Editor editor=loginrembme.edit();
                                        editor.putString("loginmsg","rememberme");
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

    private void processRequest() {
        //Creating a process request
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Build a google signin client with gso
        mgooglesigninclient = GoogleSignIn.getClient(this, gso);

    }

    private void processlogin() {
        //Calling the google signin dialog box with intent and using requestcode for acknowledgement.
        Intent signInIntent = mgooglesigninclient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //for checking the response recieved
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(),"Error in retrieving google data"+e.toString(),Toast.LENGTH_SHORT);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseUser curreUser=fauth.getCurrentUser();
        if(curreUser!=null){
            if(curreUser.isAnonymous()){
                fauth.signOut();
                dbh.deleteallingredients(curreUser.getUid());
                dbh.empty_fav(curreUser.getUid());
                dbh.empty_cart(curreUser.getUid());
                curreUser.delete();
            }
            fauth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                startActivity(new Intent(getApplicationContext(), Ingredients.class));
                                finish();
                            } else {

                                Toast.makeText(getApplicationContext(),"The email address is already in use by another account.",Toast.LENGTH_SHORT).show();

                                // If sign in fails, display a message to the user.
                                Toast.makeText(getApplicationContext(),"Problem in firebase login",Toast.LENGTH_SHORT);

                            }
                        }
                    });
        }


    }
}