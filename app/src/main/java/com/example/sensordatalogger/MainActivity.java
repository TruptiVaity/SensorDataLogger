package com.example.sensordatalogger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public final String FILE_NAME = "myfile.txt";
    private String sessionId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the view that shows Accelerometer category
        TextView accelerometer = (TextView) findViewById(R.id.accelerometer);
        //set a clicklistener on that view
        accelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new intent to open the {@Link Accelerometer ACtivity}
                sessionId = "1";
                Intent accIntent = new Intent(MainActivity.this, DisplayValues.class);
                accIntent.putExtra("EXTRA_SESSION_ID", sessionId);
                startActivity(accIntent);
            }
        });

        TextView gyroscope = (TextView) findViewById(R.id.gyroscope);

        gyroscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionId="2";
                Intent gyroIntent = new Intent(MainActivity.this, DisplayValues.class);
                gyroIntent.putExtra("EXTRA_SESSION_ID", sessionId);
                startActivity(gyroIntent);
            }
        });
        TextView magnetometer = (TextView) findViewById(R.id.magnetometer);

        magnetometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionId="3";
                Intent magnetIntent = new Intent(MainActivity.this, DisplayValues.class);
                magnetIntent.putExtra("EXTRA_SESSION_ID", sessionId);
                startActivity(magnetIntent);
            }
        });



        TextView alightsensor = (TextView) findViewById(R.id.lightsensor);

        alightsensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionId = "4";
                Intent lightIntent = new Intent(MainActivity.this, DisplayValues.class);
                lightIntent.putExtra("EXTRA_SESSION_ID", sessionId);
                startActivity(lightIntent);
            }
        });

        TextView pressure = (TextView) findViewById(R.id.pressure);

        pressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionId = "5";
                Intent pressureIntent = new Intent(MainActivity.this, DisplayValues.class);
                pressureIntent.putExtra("EXTRA_SESSION_ID", sessionId);
                startActivity(pressureIntent);

            }
        });


    }


    public void openAccList(View view){
        Intent openAccList = new Intent(this,DisplayValues.class);  // If Error occurs check this method
        startActivity(openAccList);
    }



    }

