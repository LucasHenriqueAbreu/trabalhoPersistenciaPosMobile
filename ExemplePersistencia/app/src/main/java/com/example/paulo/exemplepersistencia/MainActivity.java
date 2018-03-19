package com.example.paulo.exemplepersistencia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private RadioGroup group;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        group = findViewById(R.id.radioGroup);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        if(sp.contains("type")){
            selectNextScreen();
        }
    }

    public void selecionar(View view){
        SharedPreferences.Editor spEditor = sp.edit();
        switch (group.getCheckedRadioButtonId()){
            case R.id.rdbSqlite:
                spEditor.putInt("type", 0);
                break;
            case R.id.rdbRoom:
                spEditor.putInt("type", 1);
                break;
            case R.id.rdbFirebase:
                spEditor.putInt("type", 2);
                break;
        }
        spEditor.apply();
        selectNextScreen();
    }

    private void selectNextScreen(){
        Intent intent = null;
        switch (sp.getInt("type", 0)){
            case 0:
                intent = new Intent(this, sqliteActivity.class);
                break;
            case 1:
                intent = new Intent(this, RoomActivity.class);
                break;
            case 2:
                intent = new Intent(this, FirebaseActivity.class);
                break;
        }

        startActivity(intent);
        finish();
    }

}
