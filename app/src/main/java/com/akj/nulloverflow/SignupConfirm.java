package com.akj.nulloverflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;

public class SignupConfirm extends AppCompatActivity {
    String TAG = SignupConfirm.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_confirm);

        //사용된 email 주소를 보여주기 위해 email 주소를 가져옴
        TextView textView = findViewById(R.id.confirm_text1);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String email = bundle.getString("email");
        textView.setText(email);
        Button confirm_btn = findViewById(R.id.confirm_btn);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText confirmcode = findViewById(R.id.confirm_text2);
                String code = confirmcode.getText().toString();

                AWSMobileClient.getInstance().confirmSignUp(email, code, new Callback<SignUpResult>() {
                    @Override
                    public void onResult(SignUpResult signUpResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
                                if(!signUpResult.getConfirmationState()){
                                    final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();

                                    Toast.makeText(getApplicationContext(), "Confirm sign-up with: " + details.getDestination(), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"회원가입이 완료됐습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(SignupConfirm.this, Signin.class);
                                    startActivity(intent1);
                                    finish();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Confirm sign-up error", e);
                    }
                });
            }
        });
    }
}