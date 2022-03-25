package com.akj.nulloverflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Signin extends AppCompatActivity {
    EditText signin_ID, signin_password;
    Button signin_btn1, signin_btn2;
    String input_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signin_ID = findViewById(R.id.signin_ID);
        signin_password = findViewById(R.id.signin_password);

        signin_btn1 = findViewById(R.id.signin_button1);
        signin_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_ID = signin_ID.getText().toString();
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
