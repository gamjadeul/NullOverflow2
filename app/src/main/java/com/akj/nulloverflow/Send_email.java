package com.akj.nulloverflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Send_email extends AppCompatActivity {

    //공식문서의 '팝업 메시지 빌드 및 표시' 참조해서 send 버튼 눌렀을 때 팝업메시지 뜨게 함
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] Q_type = {"Question type not chosen", "Lost & Fond", "Complaint"};

        Spinner spinner1 = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, Q_type
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position1, long id1) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}