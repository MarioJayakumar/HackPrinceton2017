package com.example.aman.hpma;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class TextActivity extends Activity {

    private Button backBtn;
    private Button subBtn;
    private EditText reportTxt;
    private String[] list;
    private String dataString = "";
    private String nameString = "";
    private String fullString = "";

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        backBtn = (Button) findViewById(R.id.back);
        subBtn = (Button) findViewById(R.id.submit);
        reportTxt = (EditText) findViewById(R.id.report);
        reportTxt.setVerticalScrollBarEnabled(true);

        text = "age 20 height 72 weight 200 temperature 98.4 heart rate 76 blood pressure 120 / 84";
        //list = text.split(" ");
        list = MainActivity.getResult().split(" ");
        if (list.length == 16) {
            nameString += "Name: " + MainActivity.getPatientName() + "\n";
            dataString += "Age: " + list[1] + " \n";
            dataString += "Height: " + list[3] + " inches \n";
            dataString += "Weight: " + list[5] + " lbs \n";
            dataString += "Temperature: " + list[7] + " degrees F \n";
            dataString += "Heart Rate: " + list[10] + "bpm \n";
            dataString += "Blood Pressure: " + list[13] + "/" + list[15] + " mmHg \n";

            fullString = nameString + dataString;
        }
        else
            fullString = MainActivity.getResult();

        reportTxt.setText(fullString);

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
                Toast.makeText(getBaseContext(), "Sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {
                Map<String, String> dataPair = new HashMap<>();
                dataPair.put("name", MainActivity.getPatientName());
                if (list.length == 16) {
                    dataPair.put("age", list[1] + " ");
                    dataPair.put("height", list[3] + " ");
                    dataPair.put("weight", list[5] + " ");
                    dataPair.put("temperature", list[7] + " ");
                    dataPair.put("heartrate", list[10] + " ");
                    dataPair.put("bloodpressure", list[13] + "/" + list[15] + " ");
                }
                else
                    dataPair.put("age", MainActivity.getResult());
                //dataPair.put("data", MainActivity.getResult());
                StringBuffer sb = new StringBuffer();
                for(Map.Entry<String, String> entry  : dataPair.entrySet()) {
                    sb.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                            + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
                }

                String request = "http://ec2-54-196-43-181.compute-1.amazonaws.com/DocView/";
                URL url = new URL(request);
                sb.setLength(sb.length() - 1);
                String s1 = new String();
                s1 = sb.toString();

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", "" + Integer.toString(s1.getBytes().length));
                connection.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(s1);

                int code = connection.getResponseCode();
                System.out.println(code);
                wr.flush();
                wr.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }
    }
}
