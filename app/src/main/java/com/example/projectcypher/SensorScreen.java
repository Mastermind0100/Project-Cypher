package com.example.projectcypher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SensorScreen extends AppCompatActivity {

    TextView textView;
    Button connectbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_screen);
        getSupportActionBar().hide();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        textView = (TextView) findViewById(R.id.servertext);
        connectbutton = (Button) findViewById(R.id.connectionbutton);

        connectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Socket s = new Socket("192.168.1.30", 5000);
                    DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());
                    outputStream.writeUTF("Android Device Connected");
                    outputStream.flush();
                    outputStream.close();

//                    DataInputStream inputStream = new DataInputStream(s.getInputStream());
//                    String string = (String)inputStream.readUTF();
//                    textView.setText(string);
//                    textView.invalidate();
//                    textView.requestLayout();
                    s.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}