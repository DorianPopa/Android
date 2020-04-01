package com.qualitysoftware.secondapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener {

    private Button refreshButton;
    private TextView coordsTextView;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private SensorManager sensorManager;
    private Sensor sAccelerometer, sGyro, sMagno, sPressure, sLight, sTemp;

    TextView xValue, yValue, zValue, xGyroValue, yGyroValue, zGyroValue, xMagnoValue, yMagnoValue, zMagnoValue, light, pressure, temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        refreshButton = findViewById(R.id.RefreshButton);
        coordsTextView = findViewById(R.id.CoordinatesTextView);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                coordsTextView.setText("Long " + location.getLongitude() + " Lat " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        xValue = findViewById(R.id.xAccValue);
        yValue = findViewById(R.id.yAccValue);
        zValue = findViewById(R.id.zAccValue);

        xGyroValue = findViewById(R.id.xGyroValue);
        yGyroValue = findViewById(R.id.yGyroValue);
        zGyroValue = findViewById(R.id.zGyroValue);

        xMagnoValue = findViewById(R.id.xMagnoValue);
        yMagnoValue = findViewById(R.id.yMagnoValue);
        zMagnoValue = findViewById(R.id.zMagnoValue);

        light = findViewById(R.id.light);
        pressure = findViewById(R.id.pressure);
        temp = findViewById(R.id.temperature);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sAccelerometer != null)
            sensorManager.registerListener(this, sAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        else
            xValue.setText("Accelerometer failed to init");

        sGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(sGyro != null)
            sensorManager.registerListener(this, sGyro, SensorManager.SENSOR_DELAY_NORMAL);
        else
            xGyroValue.setText("Gyroscope failed to init");

        sMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(sMagno != null)
            sensorManager.registerListener(this, sMagno, SensorManager.SENSOR_DELAY_NORMAL);
        else
            xMagnoValue.setText("Magnetic Field sensor failed to init");

        sPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(sPressure != null)
            sensorManager.registerListener(this, sPressure, SensorManager.SENSOR_DELAY_NORMAL);
        else
            pressure.setText("Pressure sensor failed to init");

        sLight = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(sLight != null)
            sensorManager.registerListener(this, sLight, SensorManager.SENSOR_DELAY_NORMAL);
        else
            light.setText("Light sensor failed to init");

        sTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(sTemp != null)
            sensorManager.registerListener(this, sTemp, SensorManager.SENSOR_DELAY_NORMAL);
        else
            temp.setText("Failed");

    }

    public void onSensorChanged(SensorEvent event) {
        Sensor eventSensor = event.sensor;
        if(eventSensor.getType() == Sensor.TYPE_ACCELEROMETER){
            xValue.setText("X" + event.values[0]);
            yValue.setText("Y" + event.values[1]);
            zValue.setText("Z" + event.values[2]);
        }
        else if(eventSensor.getType() == Sensor.TYPE_GYROSCOPE){
            xGyroValue.setText("X" + event.values[0]);
            yGyroValue.setText("Y" + event.values[1]);
            zGyroValue.setText("Z" + event.values[2]);
        }
        else if(eventSensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            xMagnoValue.setText("X" + event.values[0]);
            yMagnoValue.setText("Y" + event.values[1]);
            zMagnoValue.setText("Z" + event.values[2]);
        }
        else if(eventSensor.getType() == Sensor.TYPE_PRESSURE){
            pressure.setText(event.values[0] + "mbar");
        }
        else if(eventSensor.getType() == Sensor.TYPE_LIGHT){
            light.setText(event.values[0] + "lx");
        }
        else if(eventSensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            temp.setText(event.values[0] + "°C");
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void refreshData(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                return;
            } else {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        }
    }
}
