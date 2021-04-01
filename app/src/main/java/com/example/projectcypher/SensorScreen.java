package com.example.projectcypher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class SensorScreen extends AppCompatActivity{

    private SensorManager msensorManager;
    private Sensor gyrosensor;
    private SensorEventListener gyroscopeEventListener;
    TextView textView;
    Button connectbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        textView = (TextView) findViewById(R.id.SensorData);
        connectbutton = (Button) findViewById(R.id.connectionbutton);
        msensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyrosensor = msensorManager != null ? msensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) : null;
        if(gyrosensor==null){
            Toast.makeText(getApplicationContext(), "Nah bro. That didn't work", Toast.LENGTH_SHORT);
        }

        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                String val = String.valueOf(Math.round(event.values[0])) + "\n" +
                        String.valueOf(Math.round(event.values[1])) + "\n" + String.valueOf(Math.round(event.values[2]));

                /*
                first value: move to face mobile upward: +ve values, downwards: -ve values
                second value: move to face mobile towards left: +ve values, rightwards: -ve values
                third values: rotate mobile anticlockwise: +ve, clockwise: -ve values
                */

                textView.setText(val);
                textView.invalidate();
                textView.requestLayout();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        connectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Socket s = new Socket("192.168.1.30", 5000);
                    DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());
                    outputStream.writeUTF("Android Device Connected");
                    outputStream.flush();
                    outputStream.close();

                    DataInputStream inputStream = new DataInputStream(s.getInputStream());
                    String string = (String)inputStream.readUTF();
                    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
                    s.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        msensorManager.registerListener(gyroscopeEventListener, gyrosensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause(){
        super.onPause();
        msensorManager.unregisterListener(gyroscopeEventListener);
    }
}