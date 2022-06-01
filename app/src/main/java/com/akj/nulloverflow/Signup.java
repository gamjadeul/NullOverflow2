package com.akj.nulloverflow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    private EditText signup_email, signup_password, signup_repeat_password, signup_name, signup_department;
    private Button signup_btn1;                 //회원가입 버튼


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String TAG = Signup.class.getSimpleName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_repeat_password = findViewById(R.id.signup_repeat_password);
        signup_name = findViewById(R.id.signup_name);
        signup_department = findViewById(R.id.signup_department);

        setSupportActionBar(findViewById(R.id.signUpToolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("회원가입");


        signup_btn1 = findViewById(R.id.signup_button1);
        signup_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_email = signup_email.getText().toString();
                String input_Password = signup_password.getText().toString();
                String input_RepeatPassword = signup_repeat_password.getText().toString();
                String input_name = signup_name.getText().toString();
                String input_department = signup_department.getText().toString();


                //필요한 유저 정보
                final Map<String, String> attributes = new HashMap<>();
                attributes.put("name", input_name);
                attributes.put("email", input_email);
                attributes.put("custom:department", input_department);

                //이메일이 입력되지 않았을 때
                if(input_email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                //비밀번호 조건
                else if(input_Password.length() < 6){
                    Toast.makeText(getApplicationContext(), "비밀번호는 6자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                }
                //재입력한 비밀번호가 일치하지 않을때 토스트
                else if(!(input_Password.equals(input_RepeatPassword))){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                //이름을 입력하지 않았을 때 토스트
                else if(input_name.isEmpty()){
                    Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                //학과를 입력하지 않았을 때 토스트
                else if(input_department.isEmpty()){
                    Toast.makeText(getApplicationContext(), "학과를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    AWSMobileClient.getInstance().signUp(input_email, input_Password, attributes, null, new Callback<SignUpResult>() {
                        @Override
                        public void onResult(final SignUpResult signUpResult) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
                                    if (!signUpResult.getConfirmationState()) {

                                        final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();

                                        Toast.makeText(getApplicationContext(), "인증 메일을 보냈습니다.: " + details.getDestination(), Toast.LENGTH_SHORT).show();
                                        //번호 인증 액티비티로 이동
                                        Intent intent = new Intent(Signup.this, SignupConfirm.class);
                                        intent.putExtra("email", input_email);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Sign-up done", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Sign-up error", e);

                            if (e.getMessage().contains("An account with the given email already exists.")){
                                errorMessage("주어진 이메일을 가진 계정이 이미 존재합니다.");
                            }
                            else if (e.getMessage().contains("Value at 'username' failed to satisfy constraint")){
                                errorMessage("이메일을 입력해주세요.");
                            }
                            else if (e.getMessage().contains("Invalid email address format.")){
                                errorMessage("잘못된 이메일 주소 형식입니다.");
                            }
                        }

                    });
                }
            }
            public void errorMessage(String message){
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
