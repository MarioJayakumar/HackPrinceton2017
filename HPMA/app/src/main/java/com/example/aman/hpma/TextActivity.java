package com.example.aman.hpma;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TextActivity extends Activity {

    Button backBtn;
    Button subBtn;
    EditText reportTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        backBtn = (Button) findViewById(R.id.back);
        subBtn = (Button) findViewById(R.id.submit);
        reportTxt = (EditText) findViewById(R.id.report);

        /* initialize buttons */
        backBtn.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent test = new Intent(TextActivity.this, MainActivity.class);
                startActivity(test);
            }
        });
    }
}