package com.akj.nulloverflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Signin extends AppCompatActivity {
    String TAG = Signin.class.getSimpleName();
    private EditText signin_email, signin_password;
    private Button signin_btn1, signin_btn2;        //로그인, 회원가입 버튼

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signin_email = findViewById(R.id.signin_email);
        signin_password = findViewById(R.id.signin_password);

        //이전에 로그인한 기록이 있으면 자동으로 로그인 됨
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i(TAG, userStateDetails.getUserState().toString());
                switch (userStateDetails.getUserState()){
                    case SIGNED_IN:
                        Intent intent = new Intent(Signin.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.toString());
            }
        });

        signin_btn1 = findViewById(R.id.signin_button1);
        signin_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_email = signin_email.getText().toString();
                String input_password = signin_password.getText().toString();

                AWSMobileClient.getInstance().signIn(input_email, input_password, null, new Callback<SignInResult>() {
                    @Override
                    public void onResult(SignInResult signInResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Sign-in callback state: " + signInResult.getSignInState());
                                switch(signInResult.getSignInState()) {
                                    //정상적으로 로그인 됐을경우
                                    case DONE:
                                        Intent intent = new Intent(Signin.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    //SMS를 이용한 인증이 필요한 경우 쓰이는 코드
                                    case SMS_MFA:
                                        Toast.makeText(getApplicationContext(), "Please confirm sign-in with SMS.", Toast.LENGTH_SHORT).show();
                                        break;
                                    //새로운 비밀번호가 필요한 경우
                                    case NEW_PASSWORD_REQUIRED:
                                        Toast.makeText(getApplicationContext(), "Please confirm sign-in with new password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(), "Unsupported sign-in confirmation: " + signInResult.getSignInState(), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Sign-in error", e);
                    }
                });

            }
        });
        //회원가입 화면으로 이동
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
