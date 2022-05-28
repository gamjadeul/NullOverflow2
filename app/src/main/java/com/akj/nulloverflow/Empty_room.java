package com.akj.nulloverflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Empty_room extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_room);

        String[] empty_purpose = {"스터디", "회의"};

        final String[] selected_purpose = {""};
        Spinner spinner = findViewById(R.id.purpose);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, empty_purpose
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_purpose[0] = empty_purpose[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_purpose[0] = "unknown";
            }
        });

        Button connecting_btn = (Button) findViewById(R.id.button2);
        connecting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent bluetooth_intent = new Intent(Empty_room.this, bluetooth_scanning.class);
                bluetooth_intent.putExtra("purpose", selected_purpose[0]);
                startActivity(bluetooth_intent);
            }
        });
    }
}