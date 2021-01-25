package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private Button signinbtn,regbtn;
    private SignInButton googlesignbtn;
    private int RC_SIGN_IN=101;
    private GoogleSignInClient mgooglesigninclient;
    private FirebaseAuth mauth;
    private TextView anonymoussignin;
    private  static  final  String sharedprefmsg="myprefsfile";
    private SharedPreferences shredlogret;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser curreUser=mauth.getCurrentUser();
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null)
        {
            Log.d("errormsg","from start of MainActivity Curreuser"+curreUser.getEmail());
            startActivity(new Intent(getApplicationContext(), Ingredients.class));
        }
        else if(curreUser!=null)
        {
            if(curreUser.isAnonymous())
            {
                Log.d("msg","From anooymous condition check");
                startActivity(new Intent(getApplicationContext(), Ingredients.class));
            }
            else
            {
                SharedPreferences shpret=getSharedPreferences(sharedprefmsg,0);
                if(shpret.contains("message"))
                {
                    startActivity(new Intent(getApplicationContext(), Ingredients.class));
                }
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        googlesignbtn=findViewById(R.id.btn_google_signin);
        signinbtn=findViewById(R.id.signin);
        regbtn=findViewById(R.id.registerbtn);
        anonymoussignin=findViewById(R.id.guestsignin);
        mauth=FirebaseAuth.getInstance();
        processRequest();


        anonymoussignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anysign();
                Log.d("msg","hello from anonymous");
            }
        });

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),LoginPage.class);
                startActivity(i);
            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });

        googlesignbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processlogin();
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
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mauth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), Ingredients.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(),"Problem in firebase login",Toast.LENGTH_SHORT);

                        }
                    }
                });
    }

    public void anysign()
    {
        mauth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mauth.getCurrentUser();
                            Intent succint=new Intent(getApplicationContext(), Ingredients.class);
                            startActivity(succint);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}