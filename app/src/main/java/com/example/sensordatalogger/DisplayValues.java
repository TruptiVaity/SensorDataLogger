package com.example.sensordatalogger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE;
import static android.hardware.Sensor.TYPE_GYROSCOPE;
import static android.hardware.Sensor.TYPE_LIGHT;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;
import static android.hardware.Sensor.TYPE_PRESSURE;

public class DisplayValues extends AppCompatActivity implements SensorEventListener {

    private String sessionId,eventRecorded,sensorName;
    private SensorManager mSensorManager;
    private float parameter1,parameter2,parameter3;
    private Sensor mAcc, mGyro,mMagnet,mLight,mPressure;
    private static final String FILE_NAME = "myfile.txt";
    private Button startButton, stopButton, recordEventButton, backButton;
    private TextView msg,msg2,msg3;
    PopupWindow popupWindow;
    AlertDialog.Builder builder;
    boolean showDialog = false;
    boolean startWriting = false;
    boolean recordEvent = false;
    private static final String TAG = "Display Values Activity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_list);
        sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> allSensors = mSensorManager.getSensorList(TYPE_PRESSURE);

        mAcc = mSensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
        mGyro = mSensorManager.getDefaultSensor(TYPE_GYROSCOPE);
        mMagnet = mSensorManager.getDefaultSensor(TYPE_MAGNETIC_FIELD);
        mLight = mSensorManager.getDefaultSensor(TYPE_LIGHT);
        mPressure = mSensorManager.getDefaultSensor(TYPE_PRESSURE);

        msg = (TextView) findViewById(R.id.valuetext);
        msg2 = (TextView) findViewById(R.id.valuetext2);
        msg3 = (TextView) findViewById(R.id.valuetext3);

        backButton = (Button) (findViewById(R.id.backbutton));
        startButton = (Button) (findViewById(R.id.start));
        stopButton = (Button) (findViewById(R.id.stop));
        recordEventButton = (Button) (findViewById(R.id.recordevent));

        builder = new AlertDialog.Builder(this);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWriting = true;
                saveTextFile();
                Log.v(TAG,"Start writing to File");
                onButtonShowPopupWindowClick(v);
                //showDialog = true;

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startWriting = false;
                Log.v(TAG,"Stop writing to file");
                popupWindow.dismiss();

            }
        });

        recordEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startWriting == true) {
                    Toast.makeText(getBaseContext(),"Event Recorded",Toast.LENGTH_SHORT).show();
                    recordEvent = true;
                    saveTextFile();
                    Log.v(TAG,"Recorded Event");
                }else {Toast.makeText(getBaseContext(),"Recording is not in progress",Toast.LENGTH_SHORT).show();}

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(startWriting == true) {
                    builder.setMessage("Do you want to stop recording?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    startWriting = false;
                                    Intent familyIntent = new Intent(DisplayValues.this, MainActivity.class);
                                    startActivity(familyIntent);
                                }

                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }else{
                    Intent familyIntent = new Intent(DisplayValues.this, MainActivity.class);
                    startActivity(familyIntent);
                }
            }
        });
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {

        switch (sessionId){
            case "1": //Acceleromter
                getSupportActionBar().setTitle("Accelerometer");
                sensorName = "Accelerometer";
                eventRecorded = "Acceleration Recorded";
                parameter1 = event.values[0];
                parameter2 = event.values[1];
                parameter3 = event.values[2];
                msg.setText("Acceleration minus Gx on the x-axis: " + String.valueOf(parameter1)+"\t" );
                msg2.setText("Acceleration minus Gy on the y-axis: "+String.valueOf(parameter2)+"\t");
                msg3.setText("Acceleration minus Gz on the z-axis: "+String.valueOf(parameter3)+"\n");
                keepWriting();
                break;

            case "2":  //Gyroscope
                getSupportActionBar().setTitle("Gyroscope");
                sensorName = "Gyroscope";
                eventRecorded = "Gyroscope Event Recorded";
                parameter1 = event.values[0];
                parameter2 = event.values[1];
                parameter3 = event.values[2];
                msg.setText("Value along Axis X: " + String.valueOf(parameter1)+"\t");
                msg2.setText("Value along Axis Y: " + String.valueOf(parameter2)+"\t");
                msg3.setText("Value along Axis Z: " + String.valueOf(parameter3)+"\n");
                keepWriting();
                break;

            case "3": //Magnetometer
                getSupportActionBar().setTitle("Magnetometer");
                sensorName = "Magnetometer";
                eventRecorded = "Magnetometer Event Recorded";
                parameter1 = event.values[0];
                parameter2 = event.values[1];
                parameter3 = event.values[2];
                msg.setText("Sensor Value 1: " + String.valueOf(parameter1)+"\t" );
                msg2.setText("Sensor Value 2: "+String.valueOf(parameter2)+"\t");
                msg3.setText("Sensor Value 3: "+String.valueOf(parameter3)+"\n");
                keepWriting();
                break;

            case "4": //Light Sensor
                getSupportActionBar().setTitle("Ambient Light Sensor");
                sensorName = "Ambient Light Sensor";
                eventRecorded = "Light Sensor Event Recorded";
                parameter1 = event.values[0];
                msg.setText("Light Sensor Value: " + String.valueOf(parameter1)+"\n");
                keepWriting();
                break;

            case "5": //Pressure Sensor
                getSupportActionBar().setTitle("Pressure Sensor");
                sensorName = "Pressure Sensor";
                parameter1 = event.values[0];
                eventRecorded = "Pressure Recorded";
                msg.setText("Pressure Sensor Value: " + String.valueOf(parameter1) + "\n");
                keepWriting();
                break;
        }




    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (sessionId) {
            case "1":
                mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case "2":
                mSensorManager.registerListener(this,mGyro,SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case "3":
                mSensorManager.registerListener(this,mMagnet,SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case "4":
                mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case "5":
                mSensorManager.registerListener(this,mPressure,SensorManager.SENSOR_DELAY_NORMAL);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    private void keepWriting(){
        if(startWriting == true) {
            saveTextFile();
            Log.v(TAG,"Keep Writing to file on Start Button");
        }
    }
    private void saveTextFile() {
        try {

            String fileContents = getTimeStamp().concat("\t"+sensorName).concat("\t"+msg.getText().toString()).concat("\t"+ msg2.getText().toString().concat("\t"+msg3.getText().toString()+"\n"));
            if(recordEvent == true){
                fileContents = getTimeStamp().concat("\t"+eventRecorded+"\n");
                recordEvent = false;
            }
            FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_APPEND);
            fileOutputStream.write(fileContents.getBytes());
            Log.v(TAG, "Writing...");
            fileOutputStream.close();
            File fileDir = new File(getFilesDir(), FILE_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_display_message, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        // boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height, false);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }
    public static String getTimeStamp() {
        Time currentTime = new Time();
        currentTime.setToNow();
        String sTime = currentTime.format("%Y-%m-%d %H:%M:%S");
        return sTime;
    }
}