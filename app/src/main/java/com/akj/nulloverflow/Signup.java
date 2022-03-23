package com.akj.nulloverflow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {

    EditText signup_ID, signup_password, signup_repeat_password, signup_name, signup_department, signup_email;
    TextView signup_msg;
    Button signup_btn1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_ID = findViewById(R.id.signup_ID);
        signup_password = findViewById(R.id.signup_password);
        signup_repeat_password = findViewById(R.id.signup_repeat_password);
        signup_name = findViewById(R.id.signup_name);
        signup_department = findViewById(R.id.signup_department);
        signup_email = findViewById(R.id.signup_email);

        signup_msg = findViewById(R.id.signup_text);

        signup_btn1 = findViewById(R.id.signup_button1);
        signup_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Password = signup_password.getText().toString();
                String RepeatPassword = signup_repeat_password.getText().toString();

                if(!(RepeatPassword.equals(Password))){
                    signup_msg.setVisibility(View.VISIBLE);
                    signup_msg.setText("Password dosen't match");
                }
            }
        });
    }
}
