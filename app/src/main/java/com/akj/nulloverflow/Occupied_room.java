package com.akj.nulloverflow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Occupied_room extends AppCompatActivity {

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
                .setTitle("Do you want to get noticed?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish(); //여기에 알림 보낼 수 있는 코드 작성
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
}