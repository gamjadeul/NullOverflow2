package com.akj.nulloverflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Send_email extends AppCompatActivity {
    //
    //공식문서의 '팝업 메시지 빌드 및 표시' 참조해서 send 버튼 눌렀을 때 팝업메시지 뜨게 함
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        수정자: 최종선 - MainOption.kt에서 Intent 전달할 때 해당 클래스를 사용하는데 런타임 오류가 나서 변경함
        R.layout.activity_main으로 되어 있었음, 해당하는 클래스의 activity는 activity_send_email인 것 같아 변경함
        (kotlin으로 만들어진 activity_main과 java로 만들어진 activity_main이 충돌하거나 혹은 해당하는 클래스의 activiysms activity_send_email인데 이름 달라서 오류나는지 이유는 모름)
         */
        //원래 코드 -> setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_send_email);

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