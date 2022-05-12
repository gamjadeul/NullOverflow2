package com.akj.nulloverflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
                //재입력한 비밀번호가 일치하지 않을때 토스트
                if(!(input_Password.equals(input_RepeatPassword))){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                //이름을 입력하지 않았을 때 토스트
                else if(!(input_name.isEmpty())){
                    Toast.makeText(getApplicationContext(), "이름을 입력하시오.", Toast.LENGTH_SHORT).show();
                }
                //학과를 입력하지 않았을 때 토스트
                else if(!(input_department.isEmpty())){
                    Toast.makeText(getApplicationContext(),"학과를 입력하시오.",Toast.LENGTH_SHORT).show();
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
                        }
                    });
                }
            }
        });
    }
}
