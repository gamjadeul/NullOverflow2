package com.akj.nulloverflow;

import static android.content.ContentValues.TAG;

import static com.amazonaws.http.HttpHeader.USER_AGENT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class Occupied_room extends AppCompatActivity {

    private static final int LOAD_SUCCESS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupied_room);

        Button btnAlam = findViewById(R.id.button2);
        btnAlam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    void showDialog() { //푸시알림 팝업창
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(Occupied_room.this)
                .setTitle("Do you want to be noticed?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //푸시 알림 받겠다 클릭하면 파이어베이스 주제에 구독하게 됨

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
                                        String new_url = url+"?token=qqq" +token + "&stat=true";
                                        httpConn(new_url, "POST");
                                        Toast.makeText(Occupied_room.this, "Push notifications will be sent to you", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish(); //여기에 알림 안보내는 코드 작성
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    public void  httpConn(final String mUrl, final String connMethod) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String result;
                try {
                    URL url = new URL(mUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    Log.i(TAG, "error in httpURLConnection");
                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod(connMethod);
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setUseCaches(false);

                    httpURLConnection.connect();

                    int responseStatusCode = httpURLConnection.getResponseCode();

                    Log.i(TAG, "error in responseStatusCode");

                    //버퍼 열어서 서버에서 리턴값 받아오기
                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();
                    }
                    Log.i(TAG, "error in inputStream");

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder sb = new StringBuilder();
                    String line;

                    Log.i(TAG, "error in bufferreader");
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    httpURLConnection.disconnect();
                    result = sb.toString().trim();
                    Log.i(TAG, "error in disconnection");
                } catch (Exception e) {
                    result = e.toString();
                }
                Log.i(TAG, "error in message handler");
            }
        });
        thread.start();
    }
}