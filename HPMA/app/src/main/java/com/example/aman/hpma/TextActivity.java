package com.example.aman.hpma;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class TextActivity extends Activity {

    private Button backBtn;
    private Button subBtn;
    private EditText reportTxt;
    private String[] list;
    private String finalString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        backBtn = (Button) findViewById(R.id.back);
        subBtn = (Button) findViewById(R.id.submit);
        reportTxt = (EditText) findViewById(R.id.report);
        reportTxt.setVerticalScrollBarEnabled(true);

        String test = "age 65 height 70 weight 120 temperature 98.2 heart rate 73 blood pressure 62 / 56";
        //list = test.split(" ");
        list = MainActivity.getResult().split(" ");
        if (list.length == 16) {
            finalString += "Name: " + MainActivity.getPatientName() + "\n";
            finalString += "Age: " + list[1] + "\n";
            finalString += "Height: " + list[3] + " inches\n";
            finalString += "Weight: " + list[5] + " lbs\n";
            finalString += "Temperature: " + list[7] + " degrees F\n";
            finalString += "Heart Rate: " + list[10] + "bpm\n";
            finalString += "Blood Pressure: " + list[13] + "/" + list[15] + " mmHg\n";
        }
        else
            finalString = MainActivity.getResult();

        reportTxt.setText(finalString);

        /* initialize buttons */
        backBtn.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent test = new Intent(TextActivity.this, MainActivity.class);
                startActivity(test);
            }
        });

        subBtn.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String toSend = "message";
                String urlParameters = "message=" + toSend;
                String request = "http://ec2-54-196-43-181.compute-1.amazonaws.com/DocView/";
                URL url = new URL(request);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                connection.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(urlParameters);

                int code = connection.getResponseCode();
                System.out.println(code);
                wr.flush();
                wr.close();
                connection.disconnect();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "";
        }
    }
}
