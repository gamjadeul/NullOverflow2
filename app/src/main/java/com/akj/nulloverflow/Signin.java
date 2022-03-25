package com.akj.nulloverflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signin extends AppCompatActivity {
    private EditText signin_email, signin_password;
    private Button signin_btn1, signin_btn2;        //로그인, 회원가입 버튼
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signin_email = findViewById(R.id.signin_email);
        signin_password = findViewById(R.id.signin_password);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("NullOverflow2");

        signin_btn1 = findViewById(R.id.signin_button1);
        signin_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_email = signin_email.getText().toString();
                String input_password = signin_password.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(input_email,input_password).addOnCompleteListener(Signin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(Signin.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(Signin.this,"Failed to sign in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        signin_btn2 = findViewById(R.id.signin_button2);
        signin_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signin.this,Signup.class);
                startActivity(intent);
            }
        });
    }
}
