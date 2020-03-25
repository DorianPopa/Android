package com.qualitysoftware.secondapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ListView lista;
    private String[] stringList = {"chestie", "chestie2"};
    private ArrayAdapter<String> arrayAdapter;

    EditText fileName;
    EditText fileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean darkThemeSet = preferences.getBoolean("pref_dark_theme", false);
        if(darkThemeSet)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.LightTheme);

        setContentView(R.layout.activity_main);

        lista = (ListView) findViewById(R.id.lista1);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringList);
        lista.setAdapter(arrayAdapter);
        lista.setOnItemClickListener(messageClickedHandler);

        fileName = findViewById(R.id.fileNameTextbox);
        fileContent = findViewById(R.id.fileContentTextbox);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume called");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause called");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("InstanceState change","List saved to bundle");
        outState.putStringArray("key", stringList);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("InstanceState change","List imported from saved bundle");
        if(savedInstanceState.getStringArray("key") != null)
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedInstanceState.getStringArray("key"));

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Context context = getApplicationContext();
            String[] descriptionList = {"Descriere chestie", "Descriere chestie2"};
            Toast toast = Toast.makeText(context, descriptionList[position], Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    public void openSettings(MenuItem item){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openDialog(MenuItem item) {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "Dialog");
    }

    public void openSensors(MenuItem item){
        Intent intent = new Intent(this, SensorsActivity.class);
        startActivity(intent);
    }

    public void writeFile(View v){
        File textFile = new File(getFilesDir(), fileName.getText().toString());
        try {
            try {
                FileInputStream fileInputStream = new FileInputStream(textFile);
                byte[] currentFileContentByte = new byte[(int) fileInputStream.available()];
                fileInputStream.read(currentFileContentByte);
                String currentContentString = new String(currentFileContentByte);
                fileInputStream.close();
                if (currentContentString.equals(fileContent.toString())) {
                    Toast.makeText(this, "File with the same content already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            catch(IOException e){

            }

            FileOutputStream fileOutputStream = new FileOutputStream(textFile);
            fileOutputStream.write(fileContent.toString().getBytes());
            fileOutputStream.close();

            Toast.makeText(this, "File Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

