package com.qualitysoftware.secondapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
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

public class SensorsActivity extends AppCompatActivity {

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
                coordsTextView.append("\n " + location.getLongitude() + " " + location.getLatitude());
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
