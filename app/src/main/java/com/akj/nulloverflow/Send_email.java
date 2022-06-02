package com.akj.nulloverflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Send_email extends AppCompatActivity {
    //
    //공식문서의 '팝업 메시지 빌드 및 표시' 참조해서 send 버튼 눌렀을 때 팝업메시지 뜨게 함
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        setSupportActionBar(findViewById(R.id.sendEmailToolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("문의하기");

        String[] Q_type = {"문의 종류를 선택하세요", "분실물 문의", "신고하기"};

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

        Button send_btn = (Button) findViewById(R.id.button);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Spinner mail_title = findViewById(R.id.spinner);
                EditText used_time = findViewById(R.id.editTextTime);
                EditText used_date = findViewById(R.id.used_date);
                EditText content = findViewById(R.id.editTextTextMultiLine);

                EditText userEmail = findViewById(R.id.editTextTextPersonName);

                Intent mail_intent = new Intent(Intent.ACTION_SEND);
                mail_intent.setType("*/*");

                mail_intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"gamjadeul0217@gmail.com"}); //받는사람 설정
                mail_intent.putExtra(Intent.EXTRA_SUBJECT, userEmail.getText().toString() + " - "  + mail_title.getSelectedItem().toString()
                + "(Date: " + used_date.getText().toString() + " , Time: " + used_time.getText().toString() + ")");
                mail_intent.putExtra(Intent.EXTRA_TEXT, content.getText().toString());

                startActivity(mail_intent);
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