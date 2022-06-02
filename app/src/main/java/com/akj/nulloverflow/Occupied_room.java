package com.akj.nulloverflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Occupied_room extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupied_room);

        setSupportActionBar(findViewById(R.id.occuToolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("사용 불가능 테라스");

        String room_loc = "";
        try {
            Intent intent = getIntent();
            room_loc = intent.getStringExtra("room_location");
            String room_pur = intent.getStringExtra("room_purpose");

            TextView room_location = findViewById(R.id.room_info_occupied);
            room_location.setText(room_loc);
            TextView room_purpose = findViewById(R.id.pupose_occupied);
            room_purpose.setText(room_pur);
        }catch (Exception e) {
            TextView room_location = findViewById(R.id.room_info_occupied);
            room_location.setText("정보를 받아올 수 없습니다. ");
            TextView room_purpose = findViewById(R.id.pupose_occupied);
            room_purpose.setText("정보를 받아올 수 없습니다. ");
        }


        Button btnAlam = findViewById(R.id.button2);
        String finalRoom_loc = room_loc;
        btnAlam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(finalRoom_loc);
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

    void showDialog(String room_loc) { //푸시알림 팝업창
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(Occupied_room.this)
                .setTitle("푸시알림을 받으시겠습니까?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //푸시 알림 받겠다 클릭하면 DynamoDB에 토큰, 테라스 이름정보 들어감

                        String url = "https://ptvxg97ama.execute-api.ap-northeast-2.amazonaws.com/pli/user_device_for_pushNotification";

                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("token", "Fetching FCM registration token failed", task.getException());
                                            return;
                                        }
                                        // Get new FCM registration token
                                        String token = task.getResult();
                                        // Connect to aws api gateway
                                        Log.d("token", token);
                                        String new_url = url+"?token=" + token + "&stat=true" + "&loc=" + room_loc;
                                        httpConn(new_url, "POST");
                                        Toast.makeText(Occupied_room.this, "푸시알림을 보내드립니다. ", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
    //서버연결해서 token, 테라스정보 올리기
    public void  httpConn(final String mUrl, final String connMethod) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String result;
                try {
                    URL url = new URL(mUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod(connMethod);
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setUseCaches(false);

                    httpURLConnection.connect();

                    int responseStatusCode = httpURLConnection.getResponseCode();

                    //버퍼 열어서 서버에서 리턴값 받아오기
                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();
                    }

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    httpURLConnection.disconnect();
                    result = sb.toString().trim();

                } catch (Exception e) {
                    result = e.toString();
                }
            }
        });
        thread.start();
    }
}