package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    String email;
    String emailPattern="[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"+"\\@"+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+"("+"\\."+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+")+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);
        EditText emailText=findViewById(R.id.emailText);
        Button cancelButton=findViewById(R.id.cancelButton);
        Button send=findViewById(R.id.sendButton);
        cancelButton.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailText.getText().toString().trim();
                if(email.isEmpty())
                {
//                Toast.makeText(RegisterActivity.this, "Email can't be empty", Toast.LENGTH_SHORT).show();
                    emailText.setError("Email can't be empty");
                    emailText.requestFocus();
                }
                else if(!email.matches(emailPattern))
                {
                    emailText.setError("Please enter a valid email");
                    emailText.requestFocus();
                }
                else
                {
                    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotPassword.this, "Password sent to your email", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ForgotPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}