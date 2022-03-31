package com.akj.nulloverflow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private EditText signup_email, signup_password, signup_repeat_password, signup_name, signup_department;
    private Button signup_btn1;                 //회원가입 버튼
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("NullOverflow");

        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_repeat_password = findViewById(R.id.signup_repeat_password);
        signup_name = findViewById(R.id.signup_name);
        signup_department = findViewById(R.id.signup_department);



        signup_btn1 = findViewById(R.id.signup_button1);
        signup_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_email = signup_email.getText().toString();
                String input_Password = signup_password.getText().toString();
                /*String input_RepeatPassword = signup_repeat_password.getText().toString();
                String input_name = signup_name.getText().toString();
                String input_department = signup_department.getText().toString();*/

                mFirebaseAuth.createUserWithEmailAndPassword(input_email, input_Password).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmail(firebaseUser.getEmail());
                            account.setPassword(input_Password);
                            /*account.setName(input_name);
                            account.setDepartment(input_department);*/

                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                            Toast.makeText(Signup.this,"Successfully signed up", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Signup.this,"Failed to sign up", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
